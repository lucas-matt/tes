package com.tes.messages.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.Behaviors;
import com.tes.api.SendRequest;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Status;
import com.tes.core.domain.Template;
import com.tes.db.Repository;
import com.tes.messages.MessageHandlerService;
import com.tes.messages.MessageProcessingException;
import com.tes.messages.TemplateNotFoundException;
import com.tes.messages.akka.workers.EmailActor;
import com.tes.messages.akka.workers.PidgeonActor;
import com.tes.messages.akka.workers.PushActor;
import com.tes.messages.akka.workers.SMSActor;
import com.tes.messages.akka.publisher.Message;
import com.tes.messages.akka.publisher.MessageEvent;
import com.tes.messages.akka.publisher.MessageEventAck;
import com.tes.messages.akka.publisher.MessageEventBus;
import com.tes.templates.TemplateFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Asynchronous implementation of message handler service.
 *
 * <h2>Why Akka?</h2>
 *
 * Represented as an asynchronous service, because in reality we'd likely want to decouple the request to
 * send an event, and the actual work done to send an event. For example, having a limited resource - such as
 * a pool of carrier pidgeons - would likely mean messages backing up until a resource was freed to send the message.
 *
 * In a real scenario we'd probably be looking at using queuing middleware such as RabbitMQ or SQS, but in this
 * implementation Akka provides a convenient and lightweight model that decouples in a similar way.
 *
 */
public class AkkaMessageHandlerService implements MessageHandlerService {

    private ActorSystem<MessageEvent> system;

    private ActorRef<MessageEvent> guardian;

    private ActorRef<MessageEventAck> acker;

    private Repository<TemplateSpecification> templates;

    private Repository<SendRequest> messages;

    private MessageEventBus bus = new MessageEventBus();

    /**
     * Factory for top level guardian actor.
     *
     * In the setup here, we spawn and associate all worker actors that will be supervised by the guardian
     *
     * @return guardian behavior
     */
    Behavior<MessageEvent> guardian() {
        return Behaviors
                .setup(ctx -> {

                    Map<Channel, AbstractBehavior<MessageEvent>> channels = Map.of(
                        Channel.EMAIL, new EmailActor(ctx),
                        Channel.PIDGEON, new PidgeonActor(ctx),
                        Channel.PUSH, new PushActor(ctx),
                        Channel.SMS, new SMSActor(ctx)
                    );

                    // spawn child actor to receive acknowledgement messages
                    acker = ctx.spawn(acker(), "acker");

                    // spawn each worker and assign to the appropriate channel
                    channels.forEach((chan, behavior) -> {
                        ActorRef<MessageEvent> actor = ctx.spawn(behavior, chan.name());
                        bus.subscribe(actor, chan);
                    });

                    // send message along event bus
                    return Behaviors.receiveMessage(
                        msg -> {
                            ctx.getLog().debug("Publishing message {}", msg);
                            bus.publish(msg);
                            return Behaviors.same();
                        }
                    );

                });
    }

    /**
     *
     * Factory for "acker" (pun-intended). This actor handles the async acknowledgement messages and updates
     * state appropriately
     *
     * @return response handler behavior
     */
    Behavior<MessageEventAck> acker() {
        return Behaviors.receive(MessageEventAck.class)
                .onMessage(MessageEventAck.class,
                        (ctx, ack) -> {
                            Optional<SendRequest> lkp = messages.findById(ack.message.id);
                            lkp.ifPresent((req) -> {
                                Status status = ack.acked() ? Status.SENT : Status.FAILED;
                                ctx.getLog().info("Message {} completed with status {}", req.getId(), status);
                                messages.save(
                                    req.withStatus(status)
                                );
                            });
                            return Behavior.same();
                        }
                )
                .build();
    }

    /**
     * Static factory method for this service
     * @param templates - template repository
     * @param messages - message repository
     * @return
     */
    public static AkkaMessageHandlerService create(Repository<TemplateSpecification> templates, Repository<SendRequest> messages) {
        AkkaMessageHandlerService messageService = new AkkaMessageHandlerService(templates, messages);
        messageService.init();
        return messageService;
    }

    AkkaMessageHandlerService(Repository<TemplateSpecification> templates, Repository<SendRequest> messages) {
        this.templates = templates;
        this.messages = messages;
    }

    /**
     * Initializes actor system
     */
    private void init() {
        system = ActorSystem.create(guardian(), "message-service");
        guardian = system;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SendRequest send(SendRequest message) throws MessageProcessingException, TemplateNotFoundException {
        message.setId(UUID.randomUUID());
        TemplateSpecification spec = ensureTemplate(message.getTemplate());
        ensureChannelSupported(spec, message);
        ensureDeliveryInfoSupplied(message);
        messages.save(message);
        MessageEvent evt = buildMessage(message, spec);
        guardian.tell(evt);
        return message;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<SendRequest> get(UUID id) {
        return messages.findById(id);
    }

    public void subscribe(ActorRef<MessageEvent> actorRef, Channel channel) {
        this.bus.subscribe(actorRef, channel);
    }

    void setGuardian(ActorRef<MessageEvent> guardian) {
        this.guardian = guardian;
    }

    void setAcker(ActorRef<MessageEventAck> acker) {
        this.acker = acker;
    }

    public ActorSystem<MessageEvent> getSystem() {
        return system;
    }

    private MessageEvent buildMessage(SendRequest message, TemplateSpecification spec) {
        Template template = TemplateFactory.compile(spec);
        String applied = template.apply(message.getMetadata());
        return new MessageEvent(
                new Message(message.getId(),
                        message.getDestination().getChannel(),
                        message.getDestination().getDeliveryInfo(),
                        applied),
                acker
        );
    }

    private static void ensureChannelSupported(TemplateSpecification spec, SendRequest message) throws MessageProcessingException {
        Channel channel = message.getDestination().getChannel();
        boolean supported = spec.getChannels().contains(channel);
        if (!supported) {
            throw new MessageProcessingException(
                    String.format("Template %s does not support channel %s", spec.getId(), channel)
            );
        }
    }

    private static void ensureDeliveryInfoSupplied(SendRequest message) throws MessageProcessingException {
        Set<String> deliveryKeys = message.getDestination().getDeliveryInfo().keySet();
        Channel channel = message.getDestination().getChannel();
        boolean deliveryKeysOk = channel.deliveryKeysOk(deliveryKeys);
        if (!deliveryKeysOk) {
            throw new MessageProcessingException("Delivery info missing");
        }
    }


    private TemplateSpecification ensureTemplate(UUID templateId) throws TemplateNotFoundException {
        Optional<TemplateSpecification> template = templates.findById(templateId);
        if (template.isEmpty()) {
            throw new TemplateNotFoundException(
                    String.format("Template with id %s does not exist", templateId)
            );
        }
        return template.get();
    }

}

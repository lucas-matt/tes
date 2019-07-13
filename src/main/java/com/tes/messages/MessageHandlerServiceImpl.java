package com.tes.messages;

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
import com.tes.messages.actors.EmailActor;
import com.tes.messages.actors.PidgeonActor;
import com.tes.messages.actors.PushActor;
import com.tes.messages.actors.SMSActor;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;
import com.tes.messages.publisher.MessageEventBus;
import com.tes.templates.TemplateFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MessageHandlerServiceImpl implements MessageHandlerService {

    private ActorSystem<MessageEvent> system;

    private ActorRef<MessageEvent> guardian;

    private ActorRef<MessageEventAck> acker;

    private Repository<TemplateSpecification> templates;

    private Repository<SendRequest> messages;

    private MessageEventBus bus = new MessageEventBus();

    Behavior<MessageEvent> guardian() {
        return Behaviors
                .setup(ctx -> {

                    Map<Channel, AbstractBehavior<MessageEvent>> channels = Map.of(
                        Channel.EMAIL, new EmailActor(ctx),
                        Channel.PIDGEON, new PidgeonActor(ctx),
                        Channel.PUSH, new PushActor(ctx),
                        Channel.SMS, new SMSActor(ctx)
                    );

                    acker = ctx.spawn(acker(), "acker");

                    channels.forEach((chan, behavior) -> {
                        ActorRef<MessageEvent> actor = ctx.spawn(behavior, chan.name());
                        bus.subscribe(actor, chan);
                    });

                    return Behaviors.receiveMessage(
                        msg -> {
                            bus.publish(msg);
                            return Behaviors.same();
                        }
                    );

                });
    }

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

    public static MessageHandlerServiceImpl create(Repository<TemplateSpecification> templates, Repository<SendRequest> messages) {
        MessageHandlerServiceImpl messageService = new MessageHandlerServiceImpl(templates, messages);
        messageService.init();
        return messageService;
    }

    MessageHandlerServiceImpl(Repository<TemplateSpecification> templates, Repository<SendRequest> messages) {
        this.templates = templates;
        this.messages = messages;
    }

    private void init() {
        system = ActorSystem.create(guardian(), "message-service");
        guardian = system;
    }

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
}

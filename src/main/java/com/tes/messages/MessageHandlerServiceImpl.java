package com.tes.messages;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.tes.api.SendRequest;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Template;
import com.tes.db.Repository;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;
import com.tes.messages.publisher.MessageEventBus;
import com.tes.templates.TemplateFactory;

import java.util.Optional;
import java.util.UUID;

public class MessageHandlerServiceImpl implements MessageHandlerService {

    private ActorSystem<MessageEvent> guardian;

    private ActorRef<MessageEventAck> acker;

    private Repository<TemplateSpecification> templates;

    private Repository<SendRequest> messages;

    private MessageEventBus bus = new MessageEventBus();

    private Behavior<MessageEvent> guardian() {
        return Behaviors.receive(MessageEvent.class)
                .onMessage(MessageEvent.class,
                        (ctx, evt) -> {
                            bus.publish(evt);
                            return Behavior.same();
                        }
                )
                .build();
    }

    private Behavior<MessageEventAck> acker() {
        return Behaviors.receive(MessageEventAck.class)
                .onMessage(MessageEventAck.class,
                        (ctx, ack) -> {
                            Optional<SendRequest> lkp = messages.findById(ack.event.message.id);
                            lkp.ifPresent((req) -> {
                                // set status
                            });
                            return Behavior.same();
                        }
                )
                .build();
    }

    public static MessageHandlerService create(Repository<TemplateSpecification> templates, Repository<SendRequest> messages) {
        MessageHandlerServiceImpl messageService = new MessageHandlerServiceImpl(templates, messages);
        messageService.init();
        return messageService;
    }

    private MessageHandlerServiceImpl(Repository<TemplateSpecification> templates, Repository<SendRequest> messages) {
        this.templates = templates;
        this.messages = messages;
    }

    private void init() {
        guardian = ActorSystem.create(guardian(), "message-service");
    }

    @Override
    public void send(SendRequest message) throws MessageProcessingException {
        Template template = checkAndGetTemplate(message.getTemplate());
        String applied = template.apply(message.getMetadata());

    }

    private Template checkAndGetTemplate(UUID templateId) throws MessageProcessingException {
        Optional<TemplateSpecification> template = templates.findById(templateId);
        if (template.isEmpty()) {
            throw new MessageProcessingException(
                    String.format("Template with id %s does not exist", templateId)
            );
        }
        return TemplateFactory.compile(template.get());
    }

    @Override
    public Optional<SendRequest> get(UUID id) {
        return Optional.empty();
    }

    public void subscribe(ActorRef<MessageEvent> actorRef, Channel channel) {
        this.bus.subscribe(actorRef, channel);
    }

}

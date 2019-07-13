package com.tes.messages.actors;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.tes.messages.Message;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;

import java.util.function.Supplier;

public class PidgeonActor extends AbstractBehavior<MessageEvent> {

    private final ActorContext<MessageEvent> ctx;

    private final Supplier<Boolean> flip;

    public PidgeonActor(ActorContext<MessageEvent> ctx) {
        this(ctx, () -> Math.random() > 0.5);
    }

    PidgeonActor(ActorContext<MessageEvent> ctx, Supplier flip) {
        this.ctx = ctx;
        this.flip = flip;
    }

    @Override
    public Receive<MessageEvent> createReceive() {

        ReceiveBuilder<MessageEvent> builder = newReceiveBuilder();

        builder.onMessage(
                MessageEvent.class,
                event -> {
                    var replyTo = event.sender;
                    Message message = event.message;
                    MessageEventAck resp = new MessageEventAck(message, genStatus(message));
                    replyTo.tell(resp);
                    return this;
                }
        );

        return builder.build();
    }

    private MessageEventAck.Status genStatus(Message message) {
        Boolean success = flip.get();
        if (success) {
            ctx.getLog().info("Pidgeon heading off with {} and content {}", message.id, message.body);
            return MessageEventAck.Status.ACK;
        } else {
            ctx.getLog().warning("All pidgeons snoozing, message {} failed", message.id);
            return MessageEventAck.Status.NACK;
        }
    }

}

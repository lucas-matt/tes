package com.tes.messages.akka.workers;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.tes.messages.akka.publisher.Message;
import com.tes.messages.akka.publisher.MessageEvent;
import com.tes.messages.akka.publisher.MessageEventAck;

import java.util.function.Supplier;

/**
 * Stub actor that deals with pidgeon message processing
 *
 * Simulation behaviour - will fail 50% of the time to simulate a shortage of available pidgeons
 */
public class PidgeonActor extends AbstractBehavior<MessageEvent> {

    private final ActorContext<MessageEvent> ctx;

    /**
     * Randomizer as to whether pidgeon will pickup message
     */
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

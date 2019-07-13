package com.tes.messages.actors;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.tes.messages.Message;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;

public class PushActor extends AbstractBehavior<MessageEvent> {

    private final ActorContext<MessageEvent> ctx;

    public PushActor(ActorContext<MessageEvent> ctx) {
        this.ctx = ctx;
    }

    @Override
    public Receive<MessageEvent> createReceive() {

        ReceiveBuilder<MessageEvent> builder = newReceiveBuilder();

        builder.onMessage(
                MessageEvent.class,
                event -> {
                    var replyTo = event.sender;
                    Message message = event.message;
                    MessageEventAck nack = new MessageEventAck(message, MessageEventAck.Status.NACK);
                    ctx.getLog().warning("Push service down, request failed {}", message.id);
                    replyTo.tell(nack);
                    return this;
                }
        );

        return builder.build();
    }
}

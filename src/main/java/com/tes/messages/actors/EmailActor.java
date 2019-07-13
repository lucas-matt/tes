package com.tes.messages.actors;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.tes.messages.Message;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;

public class EmailActor extends AbstractBehavior<MessageEvent> {

    private final ActorContext<MessageEvent> ctx;

    public EmailActor(ActorContext<MessageEvent> ctx) {
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
                    MessageEventAck ack = new MessageEventAck(message, MessageEventAck.Status.ACK);
                    ctx.getLog().info("Email for {} successfully sent with body {}!", message.id, message.body);
                    replyTo.tell(ack);
                    return this;
                }
        );

        return builder.build();
    }

}

package com.tes.messages.actors;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.tes.messages.Message;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;

import java.time.Duration;

public class SMSActor extends AbstractBehavior<MessageEvent> {

    private final ActorContext<MessageEvent> ctx;

    private final Duration delay;

    public SMSActor(ActorContext<MessageEvent> ctx) {
        this(ctx, Duration.ofSeconds(10));
    }

    SMSActor(ActorContext<MessageEvent> ctx, Duration delay) {
        this.ctx = ctx;
        this.delay = delay;
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
                    ctx.getLog().info("SMS sending {} with body {}", message.id, message.body);
                    ctx.scheduleOnce(delay, replyTo, ack);
                    return this;
                }
        );

        return builder.build();
    }

}

package com.tes.messages.akka.workers;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.ReceiveBuilder;
import com.tes.messages.akka.publisher.Message;
import com.tes.messages.akka.publisher.MessageEvent;
import com.tes.messages.akka.publisher.MessageEventAck;

import java.time.Duration;

/**
 * Stub actor that deals with SMS message processing
 *
 * Simulation behaviour - SMS service is running slowly, so will cause a lag of 10 seconds before message
 *                        is successfully sent
 */
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

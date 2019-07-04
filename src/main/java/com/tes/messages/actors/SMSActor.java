package com.tes.messages.actors;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.Receive;
import com.tes.messages.publisher.MessageEvent;

public class SMSActor extends AbstractBehavior<MessageEvent> {

    @Override
    public Receive<MessageEvent> createReceive() {
        return null;
    }

}

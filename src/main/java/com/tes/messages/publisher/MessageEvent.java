package com.tes.messages.publisher;

import akka.actor.ActorRef;
import com.tes.core.domain.Message;

public class MessageEvent {

    public final Message message;

    public final ActorRef sender;

    public MessageEvent(Message message, ActorRef sender) {
        this.message = message;
        this.sender = sender;
    }

}

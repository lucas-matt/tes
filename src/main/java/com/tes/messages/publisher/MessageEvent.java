package com.tes.messages.publisher;

import akka.actor.typed.ActorRef;
import com.tes.messages.Message;

public class MessageEvent {

    public final Message message;

    public final ActorRef<MessageEventAck> sender;

    public MessageEvent(Message message, ActorRef<MessageEventAck> sender) {
        this.message = message;
        this.sender = sender;
    }
}

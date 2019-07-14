package com.tes.messages.akka.publisher;

import akka.actor.typed.ActorRef;

/**
 * Immutable akka event for a message send event
 */
public class MessageEvent {

    /**
     * The message being sent
     */
    public final Message message;

    /**
     * The actor to which to reply
     */
    public final ActorRef<MessageEventAck> sender;

    public MessageEvent(Message message, ActorRef<MessageEventAck> sender) {
        this.message = message;
        this.sender = sender;
    }
}

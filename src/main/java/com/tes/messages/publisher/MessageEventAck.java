package com.tes.messages.publisher;

public class MessageEventAck {

    enum Status {
        ACK,
        NACK
    }

    public final MessageEvent event;

    public final Status status;

    public final String message;

    public MessageEventAck(MessageEvent event, Status status, String message) {
        this.event = event;
        this.status = status;
        this.message = message;
    }

}

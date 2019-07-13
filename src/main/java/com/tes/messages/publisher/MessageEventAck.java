package com.tes.messages.publisher;

import com.tes.messages.Message;

import java.util.Objects;

public class MessageEventAck {

    public enum Status {
        ACK,
        NACK
    }

    public final Message message;

    public final Status status;

    public MessageEventAck(Message message, Status status) {
        this.message = message;
        this.status = status;
    }

    public boolean acked() {
        return this.status == Status.ACK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEventAck that = (MessageEventAck) o;
        return Objects.equals(message, that.message) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, status);
    }
}

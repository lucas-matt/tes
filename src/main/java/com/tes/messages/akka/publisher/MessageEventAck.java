package com.tes.messages.akka.publisher;

import java.util.Objects;

/**
 * Immutable akka ACK event - represents a notification for message send completion
 */
public class MessageEventAck {

    /**
     * Whether the message was sent (acknowledged) or failed (not acknowledged)
     */
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

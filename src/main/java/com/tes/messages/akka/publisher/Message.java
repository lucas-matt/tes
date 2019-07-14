package com.tes.messages.akka.publisher;

import com.tes.api.SendRequest;
import com.tes.core.domain.Channel;

import java.util.Map;
import java.util.UUID;

/**
 * Immutable complete message to be sent over Akka event bus
 */
public class Message {

    /**
     * ID of send request
     */
    public final UUID id;

    /**
     * Channel to send message along
     */
    public final Channel channel;

    /**
     * Metadata to be processed by channel
     */
    public final Map<String, String> deliveryInfo;

    /**
     * Completed message body
     */
    public final String body;

    public Message(UUID id, Channel channel, Map<String, String> deliveryInfo, String body) {
        this.id = id;
        this.channel = channel;
        this.deliveryInfo = deliveryInfo;
        this.body = body;
    }

    public static Message from(SendRequest req, String body) {
        return new Message(
                req.getId(),
                req.getDestination().getChannel(),
                req.getDestination().getDeliveryInfo(),
                body
        );
    }

}

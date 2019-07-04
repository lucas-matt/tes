package com.tes.messages;

import com.tes.core.domain.Channel;

import java.util.Map;
import java.util.UUID;

public class Message {

    public final UUID id;

    public final Channel channel;

    public final Map<String, String> deliveryInfo;

    public final String body;

    public Message(UUID id, Channel channel, Map<String, String> deliveryInfo, String body) {
        this.id = id;
        this.channel = channel;
        this.deliveryInfo = deliveryInfo;
        this.body = body;
    }

}

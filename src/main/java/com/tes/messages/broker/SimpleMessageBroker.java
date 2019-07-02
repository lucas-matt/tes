package com.tes.messages.broker;

import com.tes.api.SendRequest;

import java.util.ArrayList;
import java.util.List;

public class SimpleMessageBroker implements MessageBroker {

    public static MessageBroker INSTANCE = new SimpleMessageBroker();

    private List<SelectiveConsumer<SendRequest>> consumers = new ArrayList<>();

    @Override
    public void publish(SendRequest message) {
        consumers.stream()
                .filter((p) -> p.test(message))
                .forEach((p) -> p.accept(message));
    }

    @Override
    public void subscribe(SelectiveConsumer<SendRequest> consumer) {
        consumers.add(consumer);
    }

}

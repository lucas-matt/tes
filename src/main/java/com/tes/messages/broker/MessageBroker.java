package com.tes.messages.broker;

import com.tes.api.Message;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface MessageBroker {

    void publish(Message message);

    void subscribe(Predicate<Message> filter, Consumer<Message> consumer);

}

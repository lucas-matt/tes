package com.tes.messages.broker;

import com.tes.api.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleMessageBroker implements MessageBroker {

    public static MessageBroker INSTANCE = new SimpleMessageBroker();

    private List<ConsumerPair> consumers = new ArrayList<>();

    @Override
    public void publish(Message message) {
        consumers.stream()
                .filter((p) -> p.accepts(message))
                .forEach((p) -> p.consume(message));
    }

    @Override
    public void subscribe(Predicate<Message> filter, Consumer<Message> consumer) {
        consumers.add(
                new ConsumerPair(filter, consumer)
        );
    }

    private static class ConsumerPair {

        private Predicate<Message> filter;

        private Consumer<Message> consumer;

        public ConsumerPair(Predicate<Message> filter, Consumer<Message> consumer) {
            this.filter = filter;
            this.consumer = consumer;
        }

        boolean accepts(Message message) {
            return filter.test(message);
        }

        void consume(Message message) {
            consumer.accept(message);
        }

    }

}

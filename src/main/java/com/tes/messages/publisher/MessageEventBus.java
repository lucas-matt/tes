package com.tes.messages.publisher;

import akka.actor.typed.ActorRef;
import akka.event.japi.LookupEventBus;
import com.tes.core.domain.Channel;

public class MessageEventBus extends LookupEventBus<MessageEvent, ActorRef<MessageEvent>, Channel> {

    @Override
    public int mapSize() {
        return Channel.values().length;
    }

    @Override
    public int compareSubscribers(ActorRef<MessageEvent> a, ActorRef<MessageEvent> b) {
        return a.compareTo(b);
    }

    @Override
    public Channel classify(MessageEvent event) {
        return event.message.channel;
    }

    @Override
    public void publish(MessageEvent event, ActorRef<MessageEvent> subscriber) {
        subscriber.tell(event);
    }

}



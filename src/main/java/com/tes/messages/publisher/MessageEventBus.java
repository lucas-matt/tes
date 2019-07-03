package com.tes.messages.publisher;

import akka.actor.typed.ActorRef;
import akka.event.japi.ScanningEventBus;
import com.tes.core.domain.Channel;

public class MessageEventBus extends ScanningEventBus<MessageEvent, ActorRef<MessageEvent>, Channel> {

    @Override
    public int compareClassifiers(Channel a, Channel b) {
        return a.compareTo(b);
    }

    @Override
    public int compareSubscribers(ActorRef<MessageEvent> a, ActorRef<MessageEvent> b) {
        return a.compareTo(b);
    }

    @Override
    public boolean matches(Channel classifier, MessageEvent event) {
        return event.message.getChannelSet().contains(classifier);
    }

    @Override
    public void publish(MessageEvent event, ActorRef<MessageEvent> subscriber) {
        subscriber.tell(event);
    }

}



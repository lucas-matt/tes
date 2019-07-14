package com.tes.messages.akka.publisher;

import akka.actor.typed.ActorRef;
import akka.event.japi.LookupEventBus;
import com.tes.core.domain.Channel;

/**
 * Akka event bus to allow subscriber workers to listen for certain message types
 * In this case an actor will subscribe to a specific channel so that it can
 * process the messages for that protocol.
 *
 * @see <a href="https://doc.akka.io/docs/akka/current/event-bus.html">akka documentation</a>
 *
 */
public class MessageEventBus extends LookupEventBus<MessageEvent, ActorRef<MessageEvent>, Channel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int mapSize() {
        return Channel.values().length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareSubscribers(ActorRef<MessageEvent> a, ActorRef<MessageEvent> b) {
        return a.compareTo(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Channel classify(MessageEvent event) {
        return event.message.channel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(MessageEvent event, ActorRef<MessageEvent> subscriber) {
        subscriber.tell(event);
    }

}



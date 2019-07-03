package com.tes.messages.publisher;

import akka.actor.testkit.typed.javadsl.TestInbox;
import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.typed.ActorSystem;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Message;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MessageEventBusTest {

    @ClassRule
    public static TestKitJunitResource actorSystemResource = new TestKitJunitResource();

    private final ActorSystem system = actorSystemResource.system();

    @Test
    public void shouldDeliverOnChannelMatch() {
        TestInbox<MessageEvent> inbox = TestInbox.create();
        MessageEventBus bus = new MessageEventBus();
        bus.subscribe(inbox.getRef(), Channel.SMS);

        MessageEvent event = messageFor();
        bus.publish(event);

        inbox.expectMessage(event);
    }

    private MessageEvent messageFor(Set<Channel> channels) {
        return new MessageEvent(
                    new Message(
                            Set.of(Channel.SMS),
                            ""
                    ),
                    null
            );
    }


}

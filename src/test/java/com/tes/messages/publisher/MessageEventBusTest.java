package com.tes.messages.publisher;

import akka.actor.testkit.typed.javadsl.TestInbox;
import com.tes.core.domain.Channel;
import com.tes.messages.Message;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageEventBusTest {

    @Test
    public void shouldDeliverOnChannelMatch() {
        TestInbox<MessageEvent> inbox = TestInbox.create();
        MessageEventBus bus = new MessageEventBus();
        bus.subscribe(inbox.getRef(), Channel.SMS);

        MessageEvent event = messageFor(Channel.SMS);
        bus.publish(event);

        inbox.expectMessage(event);
    }

    @Test
    public void shouldNotDeliverOnNoMatch() {
        TestInbox<MessageEvent> inbox = TestInbox.create();
        MessageEventBus bus = new MessageEventBus();
        bus.subscribe(inbox.getRef(), Channel.SMS);

        MessageEvent event = messageFor(Channel.EMAIL);
        bus.publish(event);

        assertThat(inbox.hasMessages()).isFalse();
    }

    private MessageEvent messageFor(Channel channel) {
        return new MessageEvent(
                    new Message(UUID.randomUUID(), channel, Map.of(), ""),
                    null
            );
    }


}

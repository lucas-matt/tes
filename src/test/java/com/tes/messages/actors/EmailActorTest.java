package com.tes.messages.actors;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.tes.api.SendRequest;
import com.tes.messages.Message;
import com.tes.messages.publisher.MessageEvent;
import com.tes.messages.publisher.MessageEventAck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailActorTest {

    @Test
    public void shouldRespondOnSuccessfulSend() {
        TestInbox<MessageEventAck> replyTo = TestInbox.create();

        Behavior<MessageEvent> behavior = Behaviors.setup(EmailActor::new);
        BehaviorTestKit<MessageEvent> testKit = BehaviorTestKit.create(behavior);
        testKit.run(
                new MessageEvent(
                        Message.from(
                                SendRequest.builder()
                                    .id()
                                    .build(),
                                ""
                        ),
                        replyTo.getRef()
                )
        );

        assertThat(replyTo.hasMessages()).isTrue();
        MessageEventAck actual = replyTo.receiveMessage();
        assertThat(actual.status).isEqualTo(MessageEventAck.Status.ACK);
    }

}

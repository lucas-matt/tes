package com.tes.messages.akka.workers;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.tes.api.SendRequest;
import com.tes.messages.akka.publisher.Message;
import com.tes.messages.akka.publisher.MessageEvent;
import com.tes.messages.akka.publisher.MessageEventAck;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PushActorTest {

    @Test
    public void shouldAlwaysFail() {
        TestInbox<MessageEventAck> replyTo = TestInbox.create();

        Behavior<MessageEvent> behavior = Behaviors.setup(PushActor::new);
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
        assertThat(actual.status).isEqualTo(MessageEventAck.Status.NACK);
    }

}

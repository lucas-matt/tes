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

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class PidgeonActorTest {

    @Test
    public void shouldAckOnSuccess() {
        check(() -> true, MessageEventAck.Status.ACK);
    }

    @Test
    public void shouldNackOnFailure() {
        check(() -> false, MessageEventAck.Status.NACK);
    }

    private static void check(Supplier<Boolean> flip, MessageEventAck.Status status) {
        TestInbox<MessageEventAck> replyTo = TestInbox.create();

        Behavior<MessageEvent> behavior = Behaviors.setup((ctx) -> new PidgeonActor(ctx, flip));
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
        assertThat(actual.status).isEqualTo(status);
    }

}

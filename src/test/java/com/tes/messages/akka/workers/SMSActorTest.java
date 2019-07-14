package com.tes.messages.akka.workers;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import com.tes.api.SendRequest;
import com.tes.messages.akka.publisher.Message;
import com.tes.messages.akka.publisher.MessageEvent;
import com.tes.messages.akka.publisher.MessageEventAck;
import com.typesafe.config.ConfigFactory;
import org.junit.AfterClass;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class SMSActorTest {

    private static final ActorTestKit testKit = ActorTestKit.create("test-kit", ConfigFactory.load("xyyz.conf"));

    @AfterClass
    public static void teardown() {
        testKit.shutdownTestKit();
    }

    @Test
    public void shouldBeDelayed() {
        TestProbe<MessageEventAck> replyTo = testKit.createTestProbe();

        Behavior<MessageEvent> behavior = Behaviors.setup(ctx -> new SMSActor(ctx, Duration.ofMillis(150)));
        ActorRef<MessageEvent> actor = testKit.spawn(behavior);
        MessageEvent event = new MessageEvent(
                Message.from(
                        SendRequest.builder()
                                .id()
                                .build(),
                        ""
                ),
                replyTo.getRef()
        );
        actor.tell(event);

        MessageEventAck expected = new MessageEventAck(event.message, MessageEventAck.Status.ACK);

        replyTo.expectMessage(Duration.ofMillis(1500), expected);

    }

}

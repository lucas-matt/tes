package com.tes.messages.akka;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import com.tes.api.SendRequest;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Format;
import com.tes.core.domain.Status;
import com.tes.db.InMemoryMessageRepository;
import com.tes.db.InMemoryTemplateRepository;
import com.tes.db.Repository;
import com.tes.messages.MessageProcessingException;
import com.tes.messages.TemplateNotFoundException;
import com.tes.messages.akka.AkkaMessageHandlerService;
import com.tes.messages.akka.publisher.Message;
import com.tes.messages.akka.publisher.MessageEvent;
import com.tes.messages.akka.publisher.MessageEventAck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AkkaMessageHandlerServiceTest {

    private Repository<TemplateSpecification> templates = InMemoryTemplateRepository.INSTANCE;

    private Repository<SendRequest> messages = InMemoryMessageRepository.INSTANCE;

    private AkkaMessageHandlerService handler;

    @BeforeEach
    public void setup() {
        InMemoryTemplateRepository.INSTANCE.drop();
        InMemoryMessageRepository.INSTANCE.drop();
        handler = new AkkaMessageHandlerService(templates, messages);
    }

    @Test
    public void shouldFailIfTemplateDoesntExist() {
        assertThrows(TemplateNotFoundException.class, () -> {
            SendRequest req = SendRequest.builder()
                    .id()
                    .build();
            handler.send(req);
        });
    }

    @Test
    public void shouldFailIfTemplateDoesntSupportChannel() {
        assertThrows(MessageProcessingException.class, () -> {
            TemplateSpecification template = TemplateSpecification.builder()
                    .id()
                    .channels(Channel.SMS)
                    .build();
            templates.save(template);
            SendRequest req = SendRequest.builder()
                    .id()
                    .template(template.getId())
                    .channel(Channel.EMAIL)
                    .deliveryInfo("email", "x.y@hotmail.com")
                    .build();
            handler.send(req);
        });
    }

    @Test
    public void shouldFailIfDeliveryInfoNotSupplied() {
        assertThrows(MessageProcessingException.class, () -> {
            TemplateSpecification template = TemplateSpecification.builder()
                    .id()
                    .channels(Channel.SMS)
                    .build();
            templates.save(template);
            SendRequest req = SendRequest.builder()
                    .id()
                    .template(template.getId())
                    .channel(Channel.SMS)
                    .build();
            handler.send(req);
        });
    }

    @Test
    public void shouldSendMessageToAppropriateSubscriber() throws MessageProcessingException, TemplateNotFoundException {

        BehaviorTestKit<MessageEvent> testKit = BehaviorTestKit.create(handler.guardian());
        BehaviorTestKit<MessageEventAck> replyTo = BehaviorTestKit.create(handler.acker());

        TestInbox<MessageEvent> consumer = TestInbox.create();
        handler.subscribe(consumer.getRef(), Channel.EMAIL);
        handler.setGuardian(testKit.getRef());
        handler.setAcker(replyTo.getRef());

        TemplateSpecification template = TemplateSpecification.builder()
                .id()
                .format(Format.MUSTACHE)
                .channels(Channel.EMAIL )
                .body("")
                .build();
        templates.save(template);

        SendRequest req = SendRequest.builder()
                .id()
                .template(template.getId())
                .channel(Channel.EMAIL)
                .deliveryInfo("email", "x.y@hotmail.com")
                .build();
        handler.send(req);

        testKit.runOne();
        assertThat(consumer.hasMessages()).isTrue();

    }

    @Test
    public void shouldUpdateMessageOnAck() {

        SendRequest req = SendRequest.builder()
                .id()
                .build();
        messages.save(req);

        BehaviorTestKit<MessageEventAck> testKit = BehaviorTestKit.create(handler.acker());
        testKit.run(new MessageEventAck(
            new Message(
                req.getId(),
                Channel.SMS,
                Map.of(),
                ""
            ),
            MessageEventAck.Status.ACK
        ));

        assertThat(messages.findById(req.getId()).get().getStatus()).isEqualTo(Status.SENT);

    }

    @Test
    public void shouldLoadPersistedSendRequest() {
        SendRequest req = SendRequest.builder()
                .id()
                .build();
        messages.save(req);

        Optional<SendRequest> loaded = handler.get(req.getId());
        assertThat(loaded.isPresent()).isTrue();
        loaded.ifPresent((req2) ->{
            assertThat(req).isEqualTo(req2);
        });
    }

}

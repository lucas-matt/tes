package com.tes.integration;

import com.google.common.util.concurrent.SettableFuture;
import com.tes.api.SendRequest;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Format;
import com.tes.core.domain.Status;
import com.tes.db.InMemoryMessageRepository;
import com.tes.db.InMemoryRespository;
import com.tes.db.Repository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
public class MessageResourceIT extends BaseIT {

    @Test
    public void shouldSuccessfullySendMessage() throws InterruptedException, ExecutionException, TimeoutException {
        Response tempResp = createTemplate();

        // assert creation ok
        assertThat(tempResp.getStatus()).isEqualTo(201);
        UUID tempId = tempResp.readEntity(TemplateSpecification.class).getId();

        // send message
        SendRequest send = SendRequest.builder()
                .template(tempId)
                .channel(Channel.EMAIL)
                .metadata("person", "Bob")
                .deliveryInfo("email", "x.y@hotmail.com")
                .build();

        Future<Repository.Command> captor = awaitCompletion(Status.SENT);

        Response sendResp = request("/messages")
                .post(Entity.json(send));
        assertThat(sendResp.getStatus()).isEqualTo(202);

        // assert send response
        SendRequest sent = sendResp.readEntity(SendRequest.class);
        assertThat(sent.getId()).isNotNull();
        assertThat(sent.getStatus()).isEqualTo(Status.PENDING);

        // await async response
        captor.get(500, TimeUnit.MILLISECONDS);

        // assert state changed to sent
        Response getResp = request("/messages/" + sent.getId()).get();
        SendRequest updated = getResp.readEntity(SendRequest.class);
        assertThat(updated.getId()).isEqualTo(sent.getId());
        assertThat(updated.getStatus()).isEqualTo(Status.SENT);
    }

    @Test
    public void shouldNotFoundIfNoTemplateOnSend() {
        SendRequest send = SendRequest.builder()
                .template(UUID.randomUUID())
                .channel(Channel.EMAIL)
                .metadata("person", "Bob")
                .deliveryInfo("email", "x.y@hotmail.com")
                .build();

        Response sendResp = request("/messages")
                .post(Entity.json(send));
        assertThat(sendResp.getStatus()).isEqualTo(404);
    }

    @Test
    public void shouldNotFoundIfNoMessageOnGet() {
        Response response = request("/messages/" + UUID.randomUUID())
                .get();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void shouldBadReqIfMissingInfo() {
        Response tempResp = createTemplate();
        UUID tempId = tempResp.readEntity(TemplateSpecification.class).getId();

        // send message
        SendRequest send = SendRequest.builder()
                .template(tempId)
                .channel(Channel.EMAIL)
                .metadata("person", "Bob")
                .build();

        int status = request("/messages")
                .post(Entity.json(send))
                .getStatus();

        assertThat(status).isEqualTo(400);
    }

    private Future<Repository.Command> awaitCompletion(Status status) {
        SettableFuture<Repository.Command> captor = SettableFuture.create();
        InMemoryRespository<SendRequest> repo = InMemoryMessageRepository.INSTANCE;
        repo.trigger((cmd) -> {
            Optional<SendRequest> lkp = repo.findById(cmd.getId());
            lkp.ifPresent((msg) -> {
                if (msg.getStatus() == status) {
                    captor.set(cmd);
                }
            });
        });
        return captor;
    }

    private static Response createTemplate() {
        TemplateSpecification spec = TemplateSpecification.builder()
                .channels(Channel.EMAIL)
                .format(Format.MUSTACHE)
                .body("Hello {{person}}!")
                .build();

        return request("/templates")
                .post(Entity.json(spec));
    }


}

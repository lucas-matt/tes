package com.tes.integration;

import com.tes.core.domain.Channel;
import com.tes.core.domain.Format;
import com.tes.api.TemplateSpecification;
import com.tes.api.TemplateSpecifications;
import com.tes.db.InMemoryTemplateRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tes.utils.DbUtils.createAndSave;
import static com.tes.utils.DbUtils.notExists;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
public class TemplateResourceIT extends BaseIT {

    @Test
    public void shouldGetEmptyResponseOnNoTemplates() {
        Response response = request("/templates").get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        TemplateSpecifications specs = response.readEntity(TemplateSpecifications.class);
        assertThat(specs.getTemplates()).isEmpty();
        assertThat(specs.getTotal()).isEqualTo(0);
    }

    @Test
    public void shouldGetAllAvailableTemplatesWithDefaults() {
        List<TemplateSpecification> created = IntStream.range(0, 100)
                .mapToObj((i) -> createAndSave())
                .collect(Collectors.toList());
        Response response = request("/templates").get();
        TemplateSpecifications specs = response.readEntity(TemplateSpecifications.class);
        assertThat(specs.getTemplates().size()).isEqualTo(10);
        assertThat(created).containsAll(specs.getTemplates());
        assertThat(specs.getTotal()).isEqualTo(100);
    }

    @Test
    public void shouldGetPaginatedTemplates() {
        List<TemplateSpecification> created = IntStream.range(0, 100)
                .mapToObj((i) -> createAndSave())
                .collect(Collectors.toList());

        Response responseOne = request("/templates?page=1&limit=10").get();
        TemplateSpecifications pageOne = responseOne.readEntity(TemplateSpecifications.class);
        assertThat(pageOne.getTemplates().size()).isEqualTo(10);
        assertThat(created).containsAll(pageOne.getTemplates());
        assertThat(pageOne.getTotal()).isEqualTo(100);

        Response responseTwo = request("/templates?page=2&limit=10").get();
        TemplateSpecifications pageTwo = responseTwo.readEntity(TemplateSpecifications.class);
        assertThat(pageTwo.getTemplates().size()).isEqualTo(10);
        assertThat(created).containsAll(pageTwo.getTemplates());
        assertThat(pageTwo.getTotal()).isEqualTo(100);

        assertThat(pageOne.getTemplates()).doesNotContainAnyElementsOf(pageTwo.getTemplates());
    }

    @Test
    public void shouldGetExistingTemplate() {
        TemplateSpecification spec = buildSampleTemplate();
        InMemoryTemplateRepository.INSTANCE.save(spec);

        Response response = request("/templates/" + spec.getId()).get();
        TemplateSpecification found = response.readEntity(TemplateSpecification.class);

        assertThat(found).isEqualTo(spec);
    }

    @Test
    public void shouldNotFindMissingTemplate() {
        Response response = request("/templates/" + UUID.randomUUID()).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldCreateNewTemplate() {
        TemplateSpecification spec = buildSampleTemplate();
        Response response = request("/templates").post(Entity.json(spec));
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        TemplateSpecification body = response.readEntity(TemplateSpecification.class);
        assertThat(body.getId()).isNotNull();
    }

    @Test
    public void shouldFailToCreateInvalidTemplate() {
        TemplateSpecification spec = new TemplateSpecification();
        Response response = request("/templates").post(Entity.json(spec));
        assertThat(response.getStatus()).isEqualTo(422); // stupid dropwizard default
    }

    @Test
    public void shouldUpdateExistingTemplate() {
        // create
        TemplateSpecification spec = buildSampleTemplate();
        InMemoryTemplateRepository.INSTANCE.save(spec);

        // update
        spec.setBody("something");
        request("/templates/" + spec.getId()).put(Entity.json(spec));

        // check
        Optional<TemplateSpecification> found = InMemoryTemplateRepository.INSTANCE.findById(spec.getId());
        assertThat(found.get()).isEqualTo(spec);
    }

    @Test
    public void shouldFailToUpdateNonExistantTemplate() {
        TemplateSpecification spec = buildSampleTemplate();
        spec.setId(UUID.randomUUID());
        Response response = request("/templates/" + spec.getId()).put(Entity.json(spec));
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldDeleteExistingTemplate() {
        TemplateSpecification spec = createAndSave();
        Response response = request("/templates/" + spec.getId()).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(notExists(spec.getId())).isTrue();
    }

    @Test
    public void shouldBeIdempotentOnDelete() {
        TemplateSpecification spec = createAndSave();
        Response response = request("/templates/" + spec.getId()).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Response responseTwo = request("/templates/" + spec.getId()).delete();
        assertThat(responseTwo.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        notExists(spec.getId());
    }

    private static Invocation.Builder request(String path) {
        return CLIENT.target(String.format("http://localhost:%s%s", PORT, path))
                .request();
    }

    private static TemplateSpecification buildSampleTemplate() {
        TemplateSpecification spec = new TemplateSpecification();
        spec.setBody("");
        spec.setChannels(Set.of(Channel.SMS));
        spec.setFormat(Format.MUSTACHE);
        spec.setParameters(Set.of());
        return spec;
    }

}

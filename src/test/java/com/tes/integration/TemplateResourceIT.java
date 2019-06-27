package com.tes.integration;

import com.tes.TeSApplication;
import com.tes.TeSConfiguration;
import com.tes.api.TemplateSpec;
import com.tes.api.TemplateSpecs;
import com.tes.db.InMemoryTemplateRepository;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tes.utils.DbUtils.createAndSave;
import static com.tes.utils.DbUtils.notExists;
import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
public class TemplateResourceIT {

    private static final DropwizardTestSupport<TeSConfiguration> SUPPORT =
            new DropwizardTestSupport<>(TeSApplication.class,
                    ResourceHelpers.resourceFilePath("integration-test-config.yml"));

    private static int PORT;

    private static Client CLIENT = JerseyClientBuilder.createClient();;

    @BeforeAll
    public static void beforeClass() {
        SUPPORT.before();
        PORT = SUPPORT.getPort(0);
    }

    @AfterAll
    public static void afterClass() {
        SUPPORT.after();
    }

    @BeforeEach
    public void beforeEach() {
        InMemoryTemplateRepository.INSTANCE.drop();
    }

    @Test
    public void shouldGetEmptyResponseOnNoTemplates() {
        Response response = request("/templates").get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        TemplateSpecs specs = response.readEntity(TemplateSpecs.class);
        assertThat(specs.getTemplates()).isEmpty();
        assertThat(specs.getTotal()).isEqualTo(0);
    }

    @Test
    public void shouldGetAllAvailableTemplatesWithDefaults() {
        List<TemplateSpec> created = IntStream.range(0, 100)
                .mapToObj((i) -> createAndSave())
                .collect(Collectors.toList());
        Response response = request("/templates").get();
        TemplateSpecs specs = response.readEntity(TemplateSpecs.class);
        assertThat(specs.getTemplates().size()).isEqualTo(10);
        assertThat(created).containsAll(specs.getTemplates());
        assertThat(specs.getTotal()).isEqualTo(100);
    }

    @Test
    public void shouldGetPaginatedTemplates() {
        List<TemplateSpec> created = IntStream.range(0, 100)
                .mapToObj((i) -> createAndSave())
                .collect(Collectors.toList());

        Response responseOne = request("/templates?page=1&limit=10").get();
        TemplateSpecs pageOne = responseOne.readEntity(TemplateSpecs.class);
        assertThat(pageOne.getTemplates().size()).isEqualTo(10);
        assertThat(created).containsAll(pageOne.getTemplates());
        assertThat(pageOne.getTotal()).isEqualTo(100);

        Response responseTwo = request("/templates?page=2&limit=10").get();
        TemplateSpecs pageTwo = responseTwo.readEntity(TemplateSpecs.class);
        assertThat(pageTwo.getTemplates().size()).isEqualTo(10);
        assertThat(created).containsAll(pageTwo.getTemplates());
        assertThat(pageTwo.getTotal()).isEqualTo(100);

        assertThat(pageOne.getTemplates()).doesNotContainAnyElementsOf(pageTwo.getTemplates());
    }

    @Test
    public void shouldGetExistingTemplate() {
        fail();
    }

    @Test
    public void shouldNotFindMissingTemplate() {
        Response response = request("/templates/" + UUID.randomUUID()).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldCreateNewTemplate() {
        TemplateSpec spec = new TemplateSpec();
        Response response = request("/templates").post(Entity.json(spec));
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        TemplateSpec body = response.readEntity(TemplateSpec.class);
        assertThat(body.getId()).isNotNull();
    }

    @Test
    public void shouldFailToCreateInvalidTemplate() {
        fail();
    }

    @Test
    public void shouldUpdateExistingTemplate() {
        fail();
    }

    @Test
    public void shouldFailToUpdateNonExistantTemplate() {
        fail();
    }

    @Test
    public void shouldDeleteExistingTemplate() {
        TemplateSpec spec = createAndSave();
        Response response = request("/templates/" + spec.getId()).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        notExists(spec.getId());
    }

    @Test
    public void shouldBeIdempotentOnDelete() {
        TemplateSpec spec = createAndSave();
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

}

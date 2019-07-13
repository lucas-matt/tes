package com.tes.integration;

import com.tes.TeSApplication;
import com.tes.TeSConfiguration;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Format;
import com.tes.db.InMemoryTemplateRepository;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;

public abstract class BaseIT {

    protected static final DropwizardTestSupport<TeSConfiguration> SUPPORT =
            new DropwizardTestSupport<>(TeSApplication.class,
                    ResourceHelpers.resourceFilePath("integration-test-config.yml"));

    protected static int PORT;

    protected static Client CLIENT = JerseyClientBuilder.createClient();;

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

    protected static Invocation.Builder request(String path) {
        return CLIENT.target(String.format("http://localhost:%s%s", PORT, path))
                .request();
    }

    protected static TemplateSpecification buildSampleTemplate() {
        return TemplateSpecification.builder()
                .body("")
                .channels(Channel.EMAIL)
                .format(Format.MUSTACHE)
                .build();
    }

}

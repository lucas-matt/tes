package com.tes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tes.resources.MessagesResource;
import com.tes.resources.TemplateResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class TeSApplication extends Application<TeSConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TeSApplication().run(args);
    }

    @Override
    public String getName() {
        return "TeS";
    }

    @Override
    public void initialize(final Bootstrap<TeSConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<TeSConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(TeSConfiguration configuration) {
                return configuration.getSwagger();
            }
        });
        bootstrap.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void run(final TeSConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new TemplateResource());
        environment.jersey().register(new MessagesResource());
    }

}

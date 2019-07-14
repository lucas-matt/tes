package com.tes;

import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class TeSConfiguration extends Configuration {

    private SwaggerBundleConfiguration swagger;

    public SwaggerBundleConfiguration getSwagger() {
        return swagger;
    }
}

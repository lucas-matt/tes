package com.tes;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class TeSConfiguration extends Configuration {

    private SwaggerBundleConfiguration swagger;

    public SwaggerBundleConfiguration getSwagger() {
        return swagger;
    }
}

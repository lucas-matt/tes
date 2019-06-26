package com.tes.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

@ApiModel(description = "TemplateSpec for a message")
public class TemplateSpec {

    private UUID id;

    @ApiModelProperty(value = "Identifier for template" +
            "")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

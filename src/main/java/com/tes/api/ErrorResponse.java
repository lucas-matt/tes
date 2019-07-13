package com.tes.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Basic error JSON response
 */
@ApiModel(value = "Basic error message response", description = "Body returned on error condition")
public class ErrorResponse {

    @ApiModelProperty(value = "Message describing the error state")
    @JsonProperty
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}

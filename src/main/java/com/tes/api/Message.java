package com.tes.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tes.db.Identifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@ApiModel(description = "Message to be sent")
public class Message implements Identifiable {

    @NotEmpty
    private UUID template;

    @NotNull
    private Map<String, String> metadata;

    @NotNull
    private Channel channel;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Status status;

    @ApiModelProperty(value = "Set of templates being returned")
    public UUID getTemplate() {
        return template;
    }

    public void setTemplate(UUID template) {
        this.template = template;
    }

    @ApiModelProperty(value = "Metadata to apply to the template")
    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @ApiModelProperty(value = "Channel along which to send the message")
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public void setId(UUID id) {

    }

    /**
     * Status of message
     */
    public enum Status {

        PENDING,

        SENT,

        FAILED

    }

}

package com.tes.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@ApiModel(description = "Message to be sent")
public class Message {

    @NotEmpty
    private UUID template;

    @NotNull
    private Map<String, String> metadata;

    @NotNull
    private Channel channel;

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
}

package com.tes.api;

import com.tes.core.domain.Channel;
import com.tes.db.Identifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@ApiModel(description = "Message to be sent")
public class SendRequest implements Identifiable {

    private UUID id;

    @NotEmpty
    private UUID template;

    @NotNull
    private Map<String, Object> metadata;

    @NotNull
    private Destination destination;

    @ApiModelProperty(value = "Set of templates being returned")
    public UUID getTemplate() {
        return template;
    }

    public void setTemplate(UUID template) {
        this.template = template;
    }

    @ApiModelProperty(value = "Metadata to apply to the template")
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public static class Destination {

        @NotNull
        private Channel channel;

        @NotNull
        private Map<String, String> deliveryInfo;

        @ApiModelProperty(value = "Channel along which to send the message")
        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public Map<String, String> getDeliveryInfo() {
            return deliveryInfo;
        }

        public void setDeliveryInfo(Map<String, String> deliveryInfo) {
            this.deliveryInfo = deliveryInfo;
        }
    }

}

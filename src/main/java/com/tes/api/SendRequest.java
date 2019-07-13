package com.tes.api;

import com.tes.core.domain.Channel;
import com.tes.core.domain.Status;
import com.tes.db.Identifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Request to send a message through a channel based upon a template
 */
@ApiModel(description = "Request to send a message based upon a template")
public class SendRequest implements Identifiable {

    @ApiModelProperty(access = "READ_ONLY", value = "Identifier for send request")
    private UUID id;

    @ApiModelProperty(access = "READ_ONLY", value = "Current status of the request")
    private Status status = Status.PENDING;

    @ApiModelProperty(value = "Template to be uesd for message")
    @NotNull
    private UUID template;

    @ApiModelProperty(value = "Metadata to fill associated template")
    @NotNull
    private Map<String, Object> metadata;

    @ApiModelProperty(value = "Destination information required for specific channel")
    @NotNull
    private Destination destination;

    public UUID getTemplate() {
        return template;
    }

    public void setTemplate(UUID template) {
        this.template = template;
    }

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

    public Status getStatus() {
        return status;
    }

    public SendRequest withStatus(Status status) {
        this.status = status;
        return this;
    }

    public static class Destination {

        @ApiModelProperty(value = "Channel along which to send the message")
        @NotNull
        private Channel channel;

        @ApiModelProperty(value = "Key-Value information required for delivery down the associated channel")
        @NotNull
        private Map<String, String> deliveryInfo;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendRequest that = (SendRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {

        private SendRequest req;

        private Builder() {
            this.req = new SendRequest();
            this.req.setMetadata(new HashMap<>());
            Destination destination = new Destination();
            destination.setDeliveryInfo(new HashMap<>());
            this.req.setDestination(destination);
        }

        public Builder id() {
            this.req.setId(UUID.randomUUID());
            return this;
        }

        public Builder template(UUID id) {
            this.req.setTemplate(id);
            return this;
        }

        public Builder metadata(String key, Object val) {
            this.req.getMetadata().put(key, val);
            return this;
        }

        public Builder channel(Channel channel) {
            this.req.getDestination().setChannel(channel);
            return this;
        }

        public Builder deliveryInfo(String key, String val) {
            this.req.getDestination().getDeliveryInfo().put(key, val);
            return this;
        }

        public SendRequest build() {
            return this.req;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}

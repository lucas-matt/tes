package com.tes.api;

import com.tes.core.domain.Channel;
import com.tes.core.domain.Format;
import com.tes.db.Identifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@ApiModel(description = "Template specification for a message")
public class TemplateSpecification implements Identifiable {

    @ApiModelProperty(access = "READ_ONLY", value = "Identifier for send request")
    private UUID id;

    @ApiModelProperty(value = "Template engine to use")
    @NotNull(message = "Requires a template format")
    private Format format;

    @ApiModelProperty(value = "Template body")
    @NotNull(message = "Must provide a template body")
    private String body;

    @ApiModelProperty(value = "Set of channels allowed to be used for this template")
    @NotEmpty(message = "Must provide at least one publisher for the template")
    private Set<Channel> channels;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemplateSpecification that = (TemplateSpecification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TemplateSpec{" + "id=" + id + '}';
    }

    public static class Builder {

        private TemplateSpecification spec;

        private Builder() {
            spec = new TemplateSpecification();
        }

        public Builder id(UUID id) {
            spec.setId(id);
            return this;
        }

        public Builder id() {
            spec.setId(UUID.randomUUID());
            return this;
        }

        public Builder format(Format fmt) {
            spec.setFormat(fmt);
            return this;
        }

        public Builder body(String body) {
            spec.setBody(body);
            return this;
        }

        public Builder channels(Channel... channels) {
            spec.setChannels(Set.of(channels));
            return this;
        }

        public TemplateSpecification build() {
            return spec;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}

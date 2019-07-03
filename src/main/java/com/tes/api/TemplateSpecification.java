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

@ApiModel(description = "TemplateSpec for a message")
public class TemplateSpecification implements Identifiable {

    private UUID id;

    @NotNull(message = "Requires a template format")
    private Format format;

    @NotNull(message = "Must provide a template body")
    private String body;

    @NotNull(message = "Must be provided, although can be empty")
    private Set<String> parameters;

    @NotEmpty(message = "Must provide at least one publisher for the template")
    private Set<Channel> channels;

    @ApiModelProperty(value = "Identifier for template")
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @ApiModelProperty(value = "Main template body containing core text of the template")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @ApiModelProperty(value = "Set of mandatory parameters required for the template")
    public Set<String> getParameters() {
        return parameters;
    }

    public void setParameters(Set<String> parameters) {
        this.parameters = parameters;
    }

    @ApiModelProperty(value = "Set of channels through which template can be delivered")
    public Set<Channel> getChannels() {
        return channels;
    }

    public void setChannels(Set<Channel> channels) {
        this.channels = channels;
    }

    @ApiModelProperty(value = "Templating engine to be used")
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

}

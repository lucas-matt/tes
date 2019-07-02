package com.tes.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tes.core.domain.Channel;
import com.tes.core.domain.Status;
import com.tes.db.Identifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApiModel(description = "Message to be sent")
public class SendRequest implements Identifiable {

    @NotEmpty
    private UUID template;

    @NotNull
    private Map<String, Object> metadata;

    @NotNull
    private Channel channel;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<AuditLog> log;

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

    public void log(AuditLog auditLog) {
        this.log.add(auditLog);
    }

    public static class AuditLog {

        private Channel channel;

        private Status status;

        private String comment;

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

}

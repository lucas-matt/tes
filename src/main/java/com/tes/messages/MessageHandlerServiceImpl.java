package com.tes.messages;

import com.tes.api.SendRequest;
import com.tes.api.TemplateSpecification;
import com.tes.db.Repository;
import com.tes.core.domain.Template;
import com.tes.templates.TemplateFactory;

import java.util.Optional;
import java.util.UUID;

public class MessageHandlerServiceImpl implements MessageHandlerService {

    private Repository<TemplateSpecification> templates;

    private Repository<SendRequest> messages;

    @Override
    public void send(SendRequest message) throws MessageProcessingException {
        Template template = checkAndGetTemplate(message.getTemplate());
        String applied = template.apply(message.getMetadata());
    }

    private Template checkAndGetTemplate(UUID templateId) throws MessageProcessingException {
        Optional<TemplateSpecification> template = templates.findById(templateId);
        if (template.isEmpty()) {
            throw new MessageProcessingException(
                    String.format("Template with id %s does not exist", templateId)
            );
        }
        return TemplateFactory.compile(template.get());
    }

    @Override
    public Optional<SendRequest> get(UUID id) {
        return Optional.empty();
    }

}

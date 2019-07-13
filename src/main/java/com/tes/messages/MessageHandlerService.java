package com.tes.messages;

import com.tes.api.SendRequest;

import java.util.Optional;
import java.util.UUID;

public interface MessageHandlerService {

    SendRequest send(SendRequest message) throws MessageProcessingException, TemplateNotFoundException;

    Optional<SendRequest> get(UUID id);

}

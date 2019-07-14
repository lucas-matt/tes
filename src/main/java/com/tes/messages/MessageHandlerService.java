package com.tes.messages;

import com.tes.api.SendRequest;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to send and get status about messages
 */
public interface MessageHandlerService {

    /**
     * Send a message request
     * @param message - to be sent
     * @return updated send request
     * @throws MessageProcessingException - if message cannot be processed
     * @throws TemplateNotFoundException - if referenced template is invalid
     */
    SendRequest send(SendRequest message) throws MessageProcessingException, TemplateNotFoundException;

    /**
     * Get send request
     * @param id - of send request to get
     * @return send request, if exists
     */
    Optional<SendRequest> get(UUID id);

}

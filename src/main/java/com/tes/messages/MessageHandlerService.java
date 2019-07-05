package com.tes.messages;

import com.tes.api.SendRequest;

import java.util.Optional;
import java.util.UUID;

public interface MessageHandlerService {

    void send(SendRequest message) throws MessageProcessingException;

    Optional<SendRequest> get(UUID id);

}

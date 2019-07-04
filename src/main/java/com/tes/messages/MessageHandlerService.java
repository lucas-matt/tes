package com.tes.messages;

import akka.actor.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;
import com.tes.api.SendRequest;
import com.tes.api.TemplateSpecification;
import com.tes.db.Repository;

import java.util.Optional;
import java.util.UUID;

public interface MessageHandlerService {

    void send(SendRequest message) throws MessageProcessingException;

    Optional<SendRequest> get(UUID id);

     static MessageHandlerService create(Repository<TemplateSpecification> templateRespository, Repository<SendRequest> messageRepository) {
        ActorSystem actorSystem = ActorSystem.create();
        // wire actor here
//        Behaviors.setup( ctx -> {
//            SpawnProtocol.behavior();
//        });
        return null;
    }

}

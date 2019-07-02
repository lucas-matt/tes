package com.tes.messages.broker;

import com.tes.api.SendRequest;

public interface MessageBroker {

    void publish(SendRequest message);

    void subscribe(SelectiveConsumer<SendRequest> consumer);

}

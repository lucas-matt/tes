package com.tes.messages.consumer;

import com.tes.api.SendRequest;
import com.tes.messages.broker.SelectiveConsumer;

public class SmsConsumer implements SelectiveConsumer<SendRequest> {

    @Override
    public void accept(SendRequest message) {

    }

    @Override
    public boolean test(SendRequest message) {
        return false;
    }
}

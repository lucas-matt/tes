package com.tes.db;

import com.tes.api.SendRequest;

public class InMemoryMessageRepository extends InMemoryRespository<SendRequest> {

    public static InMemoryRespository<SendRequest> INSTANCE = new InMemoryMessageRepository();

    private InMemoryMessageRepository() {
        // singleton
    }

    @Override
    protected Class<SendRequest> type() {
        return SendRequest.class;
    }
}

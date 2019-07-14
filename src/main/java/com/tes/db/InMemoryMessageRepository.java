package com.tes.db;

import com.tes.api.SendRequest;

/**
 * Basic in-memory implementation of a message repository. In reality would be a persistent store.
 */
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

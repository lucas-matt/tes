package com.tes.db;

import com.tes.api.Message;

public class InMemoryMessageRepository extends InMemoryRespository<Message> {

    public static InMemoryRespository<Message> INSTANCE = new InMemoryMessageRepository();

    private InMemoryMessageRepository() {
        // singleton
    }

}

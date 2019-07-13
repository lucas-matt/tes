package com.tes.db;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface Repository<T extends Identifiable> {

    List<T> find(int skip, int limit);

    Optional<T> findById(UUID id);

    T save(T template);

    void delete(UUID id);

    Integer count();

    boolean exists(UUID id);

    void trigger(Consumer<Command> subscriber);

    enum CommandType {
        SAVE,
        DELETE
    }

    class Command {

        private final CommandType type;

        private final UUID id;

        public Command(CommandType type, UUID id) {
            this.type = type;
            this.id = id;
        }

        public UUID getId() {
            return id;
        }
    }

}

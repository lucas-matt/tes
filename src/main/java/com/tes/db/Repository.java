package com.tes.db;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Generic persistent store of saveable entities
 * @param <T> - Generic type
 */
public interface Repository<T extends Identifiable> {

    /**
     * Paginate through all entities in the repository
     * @param skip - skip n elements
     * @param limit - retrieve page of size
     * @return page of entities
     */
    List<T> find(int skip, int limit);

    /**
     * Retrieve entity with given UUID
     * @param id - identifier of the entity
     * @return entity (if present)
     */
    Optional<T> findById(UUID id);

    /**
     * Save a copy of the given entity
     * @param entity - to be saved
     * @return on successful save
     */
    T save(T entity);

    /**
     * Delete entity with given id
     * @param id - UUID to be removed
     */
    void delete(UUID id);

    /**
     * Size of repository
     * @return - size
     */
    Integer count();

    /**
     * Returns true if entity exists
     * @param id - UUID to be checked
     * @return - true on existance
     */
    boolean exists(UUID id);

    /**
     * Add a subscriber to listen to repository triggers
     * @param subscriber - consumer to be called on trigger
     */
    void trigger(Consumer<Command> subscriber);

    /**
     * Type of action of command
     */
    enum CommandType {
        SAVE,
        DELETE
    }

    /**
     * "Case class" representing repository command
     */
    class Command {

        /**
         * Action of command
         */
        private final CommandType type;

        /**
         * Identifier of entity on which command was applied
         */
        private final UUID id;

        Command(CommandType type, UUID id) {
            this.type = type;
            this.id = id;
        }

        public UUID getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Command{" +
                    "type=" + type +
                    ", id=" + id +
                    '}';
        }

    }

}

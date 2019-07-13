package com.tes.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class InMemoryRespository<T extends Identifiable> implements Repository<T> {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryTemplateRepository.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private List<Consumer<Command>> subscribers = new ArrayList<>();

    /**
     * Use of linked hash map for predictable iteration
     */
    private Map<UUID, T> db = new LinkedHashMap<>();

    protected abstract Class<T> type();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> find(int skip, int limit) {
        return db.values().stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> findById(UUID id) {
        LOG.debug("Finding by id {}", id);
        return Optional.ofNullable(db.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T save(T entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
        UUID id = entity.getId();
        LOG.debug("Saving template {}", entity);
        db.put(id, clone(entity, type()));
        emit(CommandType.SAVE, id);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID id) {
        LOG.debug("Deleting template {}", id);
        db.remove(id);
        emit(CommandType.DELETE, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer count() {
        return db.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(UUID id) {
        return db.containsKey(id);
    }

    /**
     * Drop all members of the in-memory collection
     */
    public void drop() {
        LOG.debug("Truncating template repository -- removing {} items", db.size());
        db.clear();
    }

    public void trigger(Consumer<Command> subscriber) {
        this.subscribers.add(subscriber);
    }

    private void emit(CommandType type, UUID id) {
        Command cmd = new Command(type, id);
        subscribers.forEach(sub -> sub.accept(cmd));
    }

    private T clone(T orig, Class<T> type) {
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(orig), type);
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }

    public static class PersistenceException extends RuntimeException {
        public PersistenceException(Throwable cause) {
            super(cause);
        }
    }

}

package com.tes.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public abstract class InMemoryRespository<T extends Identifiable> implements Repository<T> {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryTemplateRepository.class);

    /**
     * Use of linked hash map for predictable iteration
     */
    private Map<UUID, T> db = new LinkedHashMap<>();

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
        LOG.debug("Saving template {}", entity);
        db.put(entity.getId(), entity);
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID id) {
        LOG.debug("Deleting template {}", id);
        db.remove(id);
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

}

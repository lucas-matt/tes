package com.tes.db;

import com.tes.api.TemplateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic in-memory implemention of a template repository. In reality would be a persistent store.
 */
public enum InMemoryTemplateRepository implements TemplateRepository {

    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryTemplateRepository.class);

    /**
     * Use of linked hash map for predictable iteration
     */
    private Map<UUID, TemplateSpec> db = new LinkedHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TemplateSpec> find(int skip, int limit) {
        return db.values().stream()
                .skip(skip)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<TemplateSpec> findById(UUID id) {
        LOG.debug("Finding template by id {}", id);
        return Optional.ofNullable(db.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemplateSpec save(TemplateSpec template) {
        if (template.getId() == null) {
            template.setId(UUID.randomUUID());
        }
        LOG.debug("Saving template {}", template);
        db.put(template.getId(), template);
        return template;
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

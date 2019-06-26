package com.tes.db;

import com.tes.api.TemplateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Basic in-memory implemention of a template repository. In reality would be a persistent store.
 */
public enum InMemoryTemplateRepository implements TemplateRepository {

    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryTemplateRepository.class);

    private Map<UUID, TemplateSpec> db = new HashMap<>();

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Optional<TemplateSpec> findById(UUID id) {
        LOG.debug("Finding template by id {}", id);
        return Optional.of(db.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(TemplateSpec template) {
        LOG.debug("Saving template {}", template);
        db.put(template.getId(), template);
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
     * Drop all members of the in-memory collection
     */
    void drop() {
        LOG.debug("Truncating template repository -- removing {} items", db.size());
        db.clear();
    }

}

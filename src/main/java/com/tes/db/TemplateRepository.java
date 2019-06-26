package com.tes.db;

import com.tes.api.TemplateSpec;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository {

    Optional<TemplateSpec> findById(UUID id);

    void save(TemplateSpec template);

    void delete(UUID id);
}

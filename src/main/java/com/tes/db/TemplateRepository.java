package com.tes.db;

import com.tes.api.TemplateSpec;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository {

    List<TemplateSpec> find(int skip, int limit);

    Optional<TemplateSpec> findById(UUID id);

    TemplateSpec save(TemplateSpec template);

    void delete(UUID id);

    Integer count();
}

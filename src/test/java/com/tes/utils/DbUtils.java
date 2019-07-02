package com.tes.utils;

import com.tes.api.TemplateSpecification;
import com.tes.db.InMemoryTemplateRepository;

import java.util.UUID;

public class DbUtils {

    public static TemplateSpecification createAndSave() {
        TemplateSpecification template = new TemplateSpecification();
        template.setId(UUID.randomUUID());
        InMemoryTemplateRepository.INSTANCE.save(template);
        return template;
    }

    public static boolean exists(UUID id) {
        return InMemoryTemplateRepository.INSTANCE.findById(id).isPresent();
    }

    public static boolean notExists(UUID id) {
        return !exists(id);
    }

}

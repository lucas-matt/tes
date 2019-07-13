package com.tes.db;

import com.tes.api.TemplateSpecification;

/**
 * Basic in-memory implemention of a template repository. In reality would be a persistent store.
 */
public class InMemoryTemplateRepository extends InMemoryRespository<TemplateSpecification> {

    public static InMemoryRespository<TemplateSpecification> INSTANCE = new InMemoryTemplateRepository();

    private InMemoryTemplateRepository() {
        // singleton
    }

    @Override
    protected Class<TemplateSpecification> type() {
        return TemplateSpecification.class;
    }
}

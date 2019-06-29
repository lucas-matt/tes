package com.tes.db;

import com.tes.api.TemplateSpec;

/**
 * Basic in-memory implemention of a template repository. In reality would be a persistent store.
 */
public class InMemoryTemplateRepository extends InMemoryRespository<TemplateSpec> {

    public static InMemoryRespository<TemplateSpec> INSTANCE = new InMemoryTemplateRepository();

    private InMemoryTemplateRepository() {
        // singleton
    }

}

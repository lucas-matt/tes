package com.tes.templates;

import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Template;

/**
 * Interface for a template engine
 */
public interface TemplateEngine {

    /**
     * Compiles a template for the given engine from the provided specification
     * @param spec - to be converted into a specific template
     * @return template for this engine
     */
    Template compile(TemplateSpecification spec);

}

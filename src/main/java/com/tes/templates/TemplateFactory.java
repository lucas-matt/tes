package com.tes.templates;

import com.tes.core.domain.Format;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Template;
import com.tes.templates.mustache.MustacheEngine;

import java.util.Map;

/**
 * Factory for templates based upon the provided format
 */
public class TemplateFactory {

    private static final Map<Format, TemplateEngine> FACTORY_MAP = Map.of(
        Format.MUSTACHE, new MustacheEngine()
    );

    /**
     * Compile a template based upon the current spec. Driven the by format of the specification
     * @param spec - to turn into a template
     * @return - template instance
     */
    public static Template compile(TemplateSpecification spec) {
        TemplateEngine engine = FACTORY_MAP.get(spec.getFormat());
        return engine.compile(spec);
    }

}

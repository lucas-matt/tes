package com.tes.templates;

import com.tes.core.domain.Format;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Template;
import com.tes.templates.mustache.MustacheEngine;

import java.util.Map;

public class TemplateFactory {

    private static final Map<Format, TemplateEngine> FACTORY_MAP = Map.of(
        Format.MUSTACHE, new MustacheEngine()
    );

    public static Template compile(TemplateSpecification spec) {
        TemplateEngine engine = FACTORY_MAP.get(spec.getFormat());
        return engine.compile(spec);
    }

}

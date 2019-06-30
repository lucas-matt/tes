package com.tes.templates;

import com.tes.api.Format;
import com.tes.api.TemplateSpec;
import com.tes.templates.mustache.MustacheEngine;

import java.util.Map;

public class TemplateFactory {

    private static final Map<Format, TemplateEngine> FACTORY_MAP = Map.of(
        Format.MUSTACHE, new MustacheEngine()
    );

    public static Template compile(TemplateSpec spec) {
        TemplateEngine engine = FACTORY_MAP.get(spec.getFormat());
        return engine.compile(spec);
    }

}

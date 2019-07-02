package com.tes.templates.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Template;
import com.tes.templates.TemplateEngine;

import java.io.StringReader;

public class MustacheEngine implements TemplateEngine {

    private static final MustacheFactory MF = new DefaultMustacheFactory();

    @Override
    public Template compile(TemplateSpecification spec) {
        StringReader body = new StringReader(spec.getBody());
        String name = spec.getId().toString();
        Mustache mustache = MF.compile(body, name);
        return new MustacheTemplate(mustache);
    }

}

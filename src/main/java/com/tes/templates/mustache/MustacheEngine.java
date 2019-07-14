package com.tes.templates.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Template;
import com.tes.templates.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;

/**
 * Engine to produce mustache templates
 */
public class MustacheEngine implements TemplateEngine {

    private static final Logger LOG = LoggerFactory.getLogger(MustacheEngine.class);

    private static final MustacheFactory MF = new DefaultMustacheFactory();

    /**
     * {@inheritDoc}
     */
    @Override
    public Template compile(TemplateSpecification spec) {
        LOG.debug("Compiling template {}", spec.getId());
        StringReader body = new StringReader(spec.getBody());
        String name = spec.getId().toString();
        Mustache mustache = MF.compile(body, name);
        LOG.debug("Successfully compiled mustache template for {}", spec.getId());
        return new MustacheTemplate(mustache);
    }

}

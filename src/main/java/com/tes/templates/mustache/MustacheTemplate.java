package com.tes.templates.mustache;

import com.github.mustachejava.Mustache;
import com.tes.core.domain.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Map;

/**
 * Template wrapper for a mustache template
 */
public class MustacheTemplate implements Template {

    private static final Logger LOG = LoggerFactory.getLogger(MustacheTemplate.class);

    private Mustache mustache;

    public MustacheTemplate(Mustache mustache) {
        this.mustache = mustache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Map<String, Object> metadata) {
        LOG.debug("Applying {}", metadata);
        StringWriter writer = new StringWriter();
        mustache.execute(writer, metadata);
        return writer.toString();
    }

}

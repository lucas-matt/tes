package com.tes.templates.mustache;

import com.github.mustachejava.Mustache;
import com.tes.core.domain.Template;

import java.io.StringWriter;
import java.util.Map;

public class MustacheTemplate implements Template {

    private Mustache mustache;

    public MustacheTemplate(Mustache mustache) {
        this.mustache = mustache;
    }

    @Override
    public String apply(Map<String, Object> metadata) {
        StringWriter writer = new StringWriter();
        mustache.execute(writer, metadata);
        String result = writer.toString();
        return result;
    }

}

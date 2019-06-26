package com.tes.templates;

import com.tes.api.TemplateSpec;

public interface TemplateEngine {

    Template compile(TemplateSpec spec);

}

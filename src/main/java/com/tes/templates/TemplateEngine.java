package com.tes.templates;

import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Template;

public interface TemplateEngine {

    Template compile(TemplateSpecification spec);

}

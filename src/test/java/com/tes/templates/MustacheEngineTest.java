package com.tes.templates;

import com.tes.api.TemplateSpecification;
import com.tes.core.domain.Format;
import com.tes.core.domain.Template;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MustacheEngineTest {

    @Test
    public void shouldCompileAndExecuteTemplate() {
        TemplateSpecification spec = template("Hello {{thing}}!");

        Template template = TemplateFactory.compile(spec);
        String result = template.apply(Map.of("thing", "World"));

        assertThat(result).isEqualTo("Hello World!");
    }

    private static TemplateSpecification template(String body) {
        return TemplateSpecification.builder()
                .id()
                .format(Format.MUSTACHE)
                .body(body)
                .build();
    }

    @Test
    public void shouldSupportDeepJsonObjects() {
        TemplateSpecification spec = template("{{person.firstname}} {{person.lastname}} lives @ {{person.address.line1}}, {{person.address.postcode}}");
        Template template = TemplateFactory.compile(spec);

        Map<String, Object> metadata = Map.of(
                "person", Map.of(
                        "firstname", "John",
                        "lastname", "Doe",
                        "address", Map.of(
                                "line1", "1 A Road",
                                "postcode", "AB12 3CD"
                        )
                )
        );
        String result = template.apply(metadata);
        assertThat(result).isEqualTo("John Doe lives @ 1 A Road, AB12 3CD");
    }

}

package com.tes.templates;

import com.tes.api.Format;
import com.tes.api.TemplateSpec;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MustacheEngineTest {

    @Test
    public void shouldCompileAndExecuteTemplate() {
        TemplateSpec spec = template("Hello {{thing}}!");

        Template template = TemplateFactory.compile(spec);
        String result = template.apply(Map.of("thing", "World"));

        assertThat(result).isEqualTo("Hello World!");
    }

    private static TemplateSpec template(String body) {
        TemplateSpec spec = new TemplateSpec();
        spec.setId(UUID.randomUUID());
        spec.setFormat(Format.MUSTACHE);
        spec.setBody(body);
        return spec;
    }

    @Test
    public void shouldSupportDeepJsonObjects() {
        TemplateSpec spec = template("{{person.firstname}} {{person.lastname}} lives @ {{person.address.line1}}, {{person.address.postcode}}");
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

package com.tes.db;

import com.tes.api.TemplateSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTemplateRepoTest {

    private TemplateRepository repo = InMemoryTemplateRepository.INSTANCE;

    @BeforeEach
    public void setup() {
        InMemoryTemplateRepository.INSTANCE.drop();
    }

    @Test
    public void shouldSaveAndLoadTemplate() {
        TemplateSpec template = createAndSave();
        Optional<TemplateSpec> reload = repo.findById(template.getId());
        assertThat(reload.get()).isEqualTo(template);
    }

    @Test
    public void shouldDeleteTemplate() {
        TemplateSpec template = createAndSave();
        repo.delete(template.getId());
        Optional<TemplateSpec> reload = repo.findById(template.getId());
        assertThat(reload.isPresent()).isFalse();
    }

    private TemplateSpec createAndSave() {
        TemplateSpec template = new TemplateSpec();
        template.setId(UUID.randomUUID());
        repo.save(template);
        return template;
    }

}

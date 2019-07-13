package com.tes.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "Set of templates")
public class TemplateSpecifications {

    @ApiModelProperty(value = "Set of templates being returned")
    @NotNull
    private List<TemplateSpecification> templates;

    @ApiModelProperty(value = "Number of templates available")
    @NotNull
    private Integer total;

    public List<TemplateSpecification> getTemplates() {
        return templates;
    }

    public Integer getTotal() {
        return total;
    }

    public TemplateSpecifications(List<TemplateSpecification> specs, int total) {
        this.templates = specs;
        this.total = total;
    }

    public TemplateSpecifications() {
        // deserialization
    }

}

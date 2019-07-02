package com.tes.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "Set of templates")
public class TemplateSpecifications {

    @NotNull
    private List<TemplateSpecification> templates;

    @NotNull
    private Integer total;

    @ApiModelProperty(value = "Set of templates being returned")
    public List<TemplateSpecification> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateSpecification> templates) {
        this.templates = templates;
    }

    @ApiModelProperty(value = "Number of templates available")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}

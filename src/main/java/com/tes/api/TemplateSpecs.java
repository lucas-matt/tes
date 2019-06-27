package com.tes.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "Set of templates")
public class TemplateSpecs {

    @NotNull
    private List<TemplateSpec> templates;

    @NotNull
    private Integer total;

    @ApiModelProperty(value = "Set of templates being returned")
    public List<TemplateSpec> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateSpec> templates) {
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

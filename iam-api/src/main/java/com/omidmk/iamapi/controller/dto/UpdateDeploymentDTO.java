package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.PlanDV;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDeploymentDTO {
    @NotNull
    private PlanDV plan;
}

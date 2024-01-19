package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.PlanDV;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDeploymentDTO {
    @NotBlank
    private String realmName;

    @NotNull
    private PlanDV plan;
}

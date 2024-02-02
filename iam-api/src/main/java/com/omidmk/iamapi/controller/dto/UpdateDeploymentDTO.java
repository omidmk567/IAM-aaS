package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateDeploymentDTO {
    @NotNull
    private PlanDV plan;

    @Pattern(regexp = "^(RUNNING|STOPPED)$")
    private DeploymentModel.State state;
}

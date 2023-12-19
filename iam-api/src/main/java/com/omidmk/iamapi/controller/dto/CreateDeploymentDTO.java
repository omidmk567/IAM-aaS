package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.PlanDV;
import lombok.Data;

@Data
public class CreateDeploymentDTO {
    private String realmName;
    private PlanDV plan;
}

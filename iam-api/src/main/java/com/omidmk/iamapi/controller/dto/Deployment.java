package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import lombok.Data;

import java.util.UUID;

@Data
public class Deployment {
    private UUID id;
    private String realmName;
    private PlanDV plan;
    private DeploymentModel.State state;
}

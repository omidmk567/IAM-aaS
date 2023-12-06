package com.omidmk.iamapi.oauth2.model;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.deployment.SupportLevelDV;
import lombok.Data;

import java.util.UUID;

@Data
public class Deployment {
    private UUID id;
    private String realmName;
    private PlanDV plan;
    private SupportLevelDV supportLevel;
    private DeploymentModel.State state;
}

package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;

public interface DeploymentService {
    DeploymentModel createDeployment(String realmName, PlanDV planDV) throws ApplicationException;

    DeploymentModel saveDeployment(DeploymentModel deployment);
}

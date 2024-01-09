package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeploymentService {
    DeploymentModel createDeployment(String realmName, PlanDV planDV) throws RealmAlreadyExistException;

    DeploymentModel saveDeployment(DeploymentModel deployment);

    Optional<DeploymentModel> findDeploymentOfUserById(UUID userId, UUID deploymentId) throws UserNotFoundException;

    List<DeploymentModel> findDeploymentsOfUser(UUID userId) throws UserNotFoundException;

    void deleteDeployment(UUID deploymentId);

    boolean isRealmAvailable(String realmName);
}

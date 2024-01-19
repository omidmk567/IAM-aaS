package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeploymentService {
    DeploymentModel createDeployment(UUID userId, String realmName, PlanDV planDV) throws RealmAlreadyExistException, UserNotFoundException;

    DeploymentModel saveDeployment(DeploymentModel deployment);

    Optional<DeploymentModel> findDeploymentOfUserById(UUID userId, UUID deploymentId) throws UserNotFoundException;

    Page<DeploymentModel> findDeploymentsOfUser(UUID userId, Pageable pageable) throws UserNotFoundException;

    void deleteDeployment(UUID deploymentId);

    boolean isRealmAvailable(String realmName);
}

package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.DeploymentNotFoundException;
import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeploymentService {
    DeploymentModel createDeployment(UserModel userModel, String realmName, PlanDV planDV) throws RealmAlreadyExistException;

    DeploymentModel saveDeployment(DeploymentModel deployment);

    DeploymentModel findDeploymentById(UUID deploymentId) throws DeploymentNotFoundException;

    DeploymentModel findDeploymentOfUser(UserModel userModel, UUID deploymentId) throws DeploymentNotFoundException;

    Page<DeploymentModel> findDeploymentsOfUser(UserModel userModel, Pageable pageable);

    Page<DeploymentModel> findAllDeployments(Pageable pageable);

    Page<DeploymentModel> findAllActiveDeployments(Pageable pageable);

    Page<DeploymentModel> findAllAssignedDeployments(Pageable pageable);

    void deleteDeployment(DeploymentModel deploymentModel);

    boolean isRealmAvailable(String realmName);
}

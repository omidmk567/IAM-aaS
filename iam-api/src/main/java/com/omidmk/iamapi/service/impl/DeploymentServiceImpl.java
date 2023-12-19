package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.repository.DeploymentRepository;
import com.omidmk.iamapi.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeploymentServiceImpl implements DeploymentService {
    private final DeploymentRepository deploymentRepository;

    @Override
    public DeploymentModel createDeployment(String realmName, PlanDV planDV) throws ApplicationException {
        Optional<DeploymentModel> foundDeployment = deploymentRepository.findByRealmName(realmName);
        // if its found and the state is FAILED_TO_DEPLOY, lets try again
        if (foundDeployment.isPresent() && !foundDeployment.get().getState().equals(DeploymentModel.State.FAILED_TO_DEPLOY))
            throw new RealmAlreadyExistException();

        var deployment = new DeploymentModel(realmName, planDV);
        deployment.setState(DeploymentModel.State.DEPLOYING);
        return deploymentRepository.save(deployment);
    }

    @Override
    public boolean isRealmAvailable(String realmName) {
        Optional<DeploymentModel> foundDeployment = deploymentRepository.findByRealmName(realmName);
        return foundDeployment.isEmpty() || foundDeployment.get().getState().equals(DeploymentModel.State.FAILED_TO_DEPLOY);
    }

    @Override
    public DeploymentModel saveDeployment(DeploymentModel deployment) {
        return deploymentRepository.save(deployment);
    }
}

package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.DeploymentNotFoundException;
import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.DeploymentRepository;
import com.omidmk.iamapi.service.DeploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeploymentServiceImpl implements DeploymentService {
    private final DeploymentRepository deploymentRepository;

    @Override
    public DeploymentModel createDeployment(UserModel userModel, String realmName, PlanDV planDV) throws RealmAlreadyExistException {
        if (!isRealmAvailable(realmName))
            throw new RealmAlreadyExistException();

        Optional<DeploymentModel> foundDeployment = deploymentRepository.findByRealmName(realmName);
        // if its found and the state is FAILED_TO_DEPLOY, lets try again
        foundDeployment.ifPresent(deploymentModel -> {
            deploymentModel.setState(DeploymentModel.State.DEPLOYING);
            deploymentModel.setPlan(planDV);
        });
        var deployment = foundDeployment.orElseGet(() -> new DeploymentModel(userModel, realmName, planDV, DeploymentModel.State.DEPLOYING));

        return deploymentRepository.save(deployment);
    }

    @Override
    public boolean isRealmAvailable(String realmName) {
        if ("master".equals(realmName)) return false;
        Optional<DeploymentModel> foundDeployment = deploymentRepository.findByRealmName(realmName);
        return foundDeployment.isEmpty() || foundDeployment.get().getState().equals(DeploymentModel.State.FAILED_TO_DEPLOY);
    }

    public DeploymentModel findDeploymentOfUser(UserModel userModel, UUID deploymentId) throws DeploymentNotFoundException {
        return deploymentRepository.findByIdAndUser(deploymentId, userModel).orElseThrow(DeploymentNotFoundException::new);
    }

    @Override
    public Page<DeploymentModel> findDeploymentsOfUser(UserModel userModel, Pageable pageable) {
        return deploymentRepository.findAllByUser(userModel, pageable);
    }

    @Override
    public Page<DeploymentModel> findAllActiveDeployments(Pageable pageable) {
        return deploymentRepository.findAllByState(DeploymentModel.State.DEPLOYED, pageable);
    }

    @Override
    public void deleteDeployment(UUID deploymentId) {
        deploymentRepository.deleteById(deploymentId);
    }

    @Override
    public void deleteDeployment(DeploymentModel deploymentModel) {
        deploymentRepository.delete(deploymentModel);
    }

    @Override
    public DeploymentModel saveDeployment(DeploymentModel deployment) {
        return deploymentRepository.save(deployment);
    }
}

package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.DeploymentRepository;
import com.omidmk.iamapi.service.CustomerService;
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
    private final CustomerService customerService;

    @Override
    public DeploymentModel createDeployment(String realmName, PlanDV planDV) throws RealmAlreadyExistException {
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

    public Optional<DeploymentModel> findDeploymentOfUserById(UUID userId, UUID deploymentId) throws UserNotFoundException {
        Optional<UserModel> user = customerService.findUserById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException();

        return deploymentRepository.findByIdAndUser(deploymentId, user.get());
    }

    @Override
    public Page<DeploymentModel> findDeploymentsOfUser(UUID userId, Pageable pageable) throws UserNotFoundException {
        Optional<UserModel> user = customerService.findUserById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException();

        return deploymentRepository.findAllByUser(user.get(), pageable);
    }

    @Override
    public void deleteDeployment(UUID deploymentId) {
        deploymentRepository.deleteById(deploymentId);
    }

    @Override
    public DeploymentModel saveDeployment(DeploymentModel deployment) {
        return deploymentRepository.save(deployment);
    }
}

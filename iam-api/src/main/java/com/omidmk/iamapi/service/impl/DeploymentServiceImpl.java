package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.RealmAlreadyExistException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.DeploymentRepository;
import com.omidmk.iamapi.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public DeploymentModel createDeployment(UUID userId, String realmName, PlanDV planDV) throws RealmAlreadyExistException, UserNotFoundException {
        UserModel userModel = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Optional<DeploymentModel> foundDeployment = deploymentRepository.findByRealmName(realmName);
        if (!isRealmAvailable(realmName))
            throw new RealmAlreadyExistException();

        // if its found and the state is FAILED_TO_DEPLOY, lets try again
        var deployment = foundDeployment.orElseGet(() -> new DeploymentModel(userModel, realmName, planDV, DeploymentModel.State.DEPLOYING));
        userModel.getDeployments().add(deployment);
        return userRepository.save(userModel).getDeployments()
                .stream()
                .filter(d -> d.getRealmName().equals(realmName))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public boolean isRealmAvailable(String realmName) {
        if ("master".equals(realmName)) return false;
        Optional<DeploymentModel> foundDeployment = deploymentRepository.findByRealmName(realmName);
        return foundDeployment.isEmpty() || foundDeployment.get().getState().equals(DeploymentModel.State.FAILED_TO_DEPLOY);
    }

    public Optional<DeploymentModel> findDeploymentOfUserById(UUID userId, UUID deploymentId) throws UserNotFoundException {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return deploymentRepository.findByIdAndUser(deploymentId, user);
    }

    @Override
    public Page<DeploymentModel> findDeploymentsOfUser(UUID userId, Pageable pageable) throws UserNotFoundException {
        UserModel user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return deploymentRepository.findAllByUser(user, pageable);
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

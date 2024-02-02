package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeploymentRepository extends JpaRepository<DeploymentModel, UUID> {
    Optional<DeploymentModel> findByRealmName(String realmName);

    Optional<DeploymentModel> findByIdAndUserAndStateNot(UUID id, UserModel user, DeploymentModel.State state);

    Page<DeploymentModel> findAllByUserAndStateNot(UserModel user, DeploymentModel.State state, Pageable pageable);

    Page<DeploymentModel> findAllByState(DeploymentModel.State state, Pageable pageable);

    Page<DeploymentModel> findAllByStateIn(Collection<DeploymentModel.State> state, Pageable pageable);
}

package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeploymentRepository extends JpaRepository<DeploymentModel, UUID> {
    Optional<DeploymentModel> findByRealmName(String realmName);

    Optional<DeploymentModel> findByIdAndUser(UUID id, UserModel user);

    Page<DeploymentModel> findAllByUser(UserModel user, Pageable pageable);

    Page<DeploymentModel> findAllByState(DeploymentModel.State state, Pageable pageable);
}

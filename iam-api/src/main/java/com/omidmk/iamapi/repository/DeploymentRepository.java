package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeploymentRepository extends JpaRepository<DeploymentModel, UUID> {
    Optional<DeploymentModel> findByRealmName(String realmName);

    Optional<DeploymentModel> findByIdAndUser(UUID id, UserModel user);

    List<DeploymentModel> findAllByUser(UserModel user);
}

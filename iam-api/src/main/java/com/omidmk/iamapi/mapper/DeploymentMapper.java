package com.omidmk.iamapi.mapper;

import com.omidmk.iamapi.controller.dto.Deployment;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DeploymentMapper {
    Deployment deploymentModelToDeployment(DeploymentModel deploymentModel);

    List<Deployment> deploymentModelListToDeploymentList(List<DeploymentModel> deployments);
}

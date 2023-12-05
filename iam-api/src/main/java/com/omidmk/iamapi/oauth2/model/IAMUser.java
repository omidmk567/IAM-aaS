package com.omidmk.iamapi.oauth2.model;

import com.omidmk.iamapi.model.deployment.DeploymentModel;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class IAMUser {
    private UUID id;
    private String email;
    private Long balance;
    private List<DeploymentModel> deployments;
}

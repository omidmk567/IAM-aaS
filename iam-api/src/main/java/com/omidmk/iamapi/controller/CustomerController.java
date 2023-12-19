package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.config.KeycloakProperties;
import com.omidmk.iamapi.controller.dto.CreateDeploymentDTO;
import com.omidmk.iamapi.controller.dto.Customer;
import com.omidmk.iamapi.controller.dto.Deployment;
import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.mapper.DeploymentMapper;
import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import com.omidmk.iamapi.service.DeploymentService;
import com.omidmk.iamapi.service.KeycloakService;
import com.omidmk.iamapi.service.MailService;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final DeploymentService deploymentService;
    private final KeycloakService keycloakService;
    private final MailService mailService;
    private final KeycloakProperties keycloakProperties;
    private final DeploymentMapper deploymentMapper;
    private final UserMapper userMapper;

    @PostMapping("/deployments")
    @ResponseStatus(HttpStatus.CREATED)
    public Deployment createDeployment(@AuthenticationPrincipal IAMUser user, @RequestBody CreateDeploymentDTO requestBody) throws ApplicationException {
        DeploymentModel deployment = deploymentService.createDeployment(requestBody.getRealmName(), requestBody.getPlan());
        try {
            final var passwordBytes = new byte[16];
            new SecureRandom().nextBytes(passwordBytes);
            final String username = "admin";
            final String password = new String(passwordBytes);
            final String realmUrl = STR."\{keycloakProperties.getBaseUrl()}/admin/\{requestBody.getRealmName()}/console";
            keycloakService.createRealm(requestBody.getRealmName());
            keycloakService.createAdminUser(requestBody.getRealmName(), username, password, true);
            mailService.sendCustomerCredentials(user.getEmail(), username, password, realmUrl);
            deployment.setState(DeploymentModel.State.DEPLOYED);
        } catch (ApplicationException ex) {
            log.error("Application Error occurred on creating deployment. {}", ex.getMessage(), ex);
            deployment.setState(DeploymentModel.State.FAILED_TO_DEPLOY);
        } catch (RuntimeException ex) {
            log.error("Runtime Error occurred on creating deployment. {}", ex.getMessage(), ex);
            deployment.setState(DeploymentModel.State.FAILED_TO_DEPLOY);
        } finally {
            deployment = deploymentService.saveDeployment(deployment);
        }
        return deploymentMapper.deploymentModelToDeployment(deployment);
    }

    @GetMapping("/deployments")
    public List<Deployment> getDeployments(@AuthenticationPrincipal IAMUser user) {
        return deploymentMapper.deploymentModelListToDeploymentList(user.getDeployments());
    }

    @GetMapping("/deployments/available")
    public boolean isRealmAvailable(@QueryParam("realmName") String realmName) {
        return deploymentService.isRealmAvailable(realmName);
    }

    @GetMapping("/userinfo")
    public Customer getUserInfo(@AuthenticationPrincipal IAMUser user) {
        return userMapper.iamUserToCustomer(user);
    }
}

package com.omidmk.iamapi.scheduler;

import com.omidmk.iamapi.exception.RealmNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.deployment.PlanDV;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.service.CustomerService;
import com.omidmk.iamapi.service.DeploymentService;
import com.omidmk.iamapi.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeploymentsScheduler {
    private final CustomerService customerService;
    private final DeploymentService deploymentService;
    private final KeycloakService keycloakService;

    @Value("${app.iam-aas.cost-calculator-interval:1h}")
    private Duration interval;

    @Value("${app.iam-aas.cost-factor:1}")
    private float factor;

    @Scheduled(
            initialDelayString = "#{T(org.springframework.boot.convert.ApplicationConversionService).getSharedInstance().convert('${app.iam-aas.cost-calculator-interval}', T(java.time.Duration)).toMillis()}",
            fixedDelayString = "#{T(org.springframework.boot.convert.ApplicationConversionService).getSharedInstance().convert('${app.iam-aas.cost-calculator-interval}', T(java.time.Duration)).toMillis()}"
    )
    public void calculateCost() {
        log.info("About to calculate the deployments cost...");
        Page<DeploymentModel> allActiveDeployments = deploymentService.findAllActiveDeployments(Pageable.unpaged());
        allActiveDeployments.forEach(deployment -> {
            log.debug("Calculating the cost of {}", deployment);
            float costPerInterval = interval.getSeconds() * ((float) deployment.getPlan().getCostPerHour() / (60 * 60));
            UserModel user = deployment.getUser();
            user.setBalance((user.getBalance() - (costPerInterval * factor)));
            user = customerService.saveUser(user);
            if (user.getBalance() <= 0) {
                log.info("Deployment with realm name {} of user {} is going to be disabled. Current balance: {}", deployment.getRealmName(), user.getEmail(), user.getBalance());
                try {
                    keycloakService.disableRealm(deployment.getRealmName());
                    deployment.setState(DeploymentModel.State.STOPPED);
                } catch (RealmNotFoundException e) {
                    log.warn("Tried to delete the realm {} but not found!", deployment.getRealmName(), e);
                } catch (RuntimeException e) {
                    log.error("Error while disabling the realm {}", deployment.getRealmName(), e);
                }
                deploymentService.saveDeployment(deployment);
            }
        });
    }

    @Scheduled(
            initialDelayString = "#{T(org.springframework.boot.convert.ApplicationConversionService).getSharedInstance().convert('${app.iam-aas.plan-checker-interval}', T(java.time.Duration)).toMillis()}",
            fixedDelayString = "#{T(org.springframework.boot.convert.ApplicationConversionService).getSharedInstance().convert('${app.iam-aas.plan-checker-interval}', T(java.time.Duration)).toMillis()}"
    )
    public void checkDeploymentsPlan() {
        log.info("About to check the deployment plans...");
        Page<DeploymentModel> allActiveDeployments = deploymentService.findAllActiveDeployments(Pageable.unpaged());
        allActiveDeployments.forEach(deployment -> {
            log.debug("Checking the deployment plan of {}", deployment);
            String realmName = deployment.getRealmName();
            PlanDV currentPlan = deployment.getPlan();
            try {
                int usersCount = keycloakService.getRealmUsersCount(realmName);
                int clientsCount = keycloakService.getRealmClientsCount(realmName);
                int groupsCount = keycloakService.getRealmGroupsCount(realmName);
                int rolesCount = keycloakService.getRealmRolesCount(realmName);
                // If the current plan is not enough, change it to a better one
                if (currentPlan.getUsersCount() < usersCount || currentPlan.getClientsCount() < clientsCount || currentPlan.getGroupsCount() < groupsCount || currentPlan.getRolesCount() < rolesCount) {
                    PlanDV newPlan = determinePlan(usersCount, clientsCount, groupsCount, rolesCount);
                    deployment.setPlan(newPlan);
                    log.debug("Deployment plan of {} changed to {}", deployment, newPlan);
                }
            } catch (RealmNotFoundException e) {
                log.error("Error while checking the deployment plan of {}. Realm {} not found!", deployment, realmName, e);
            } catch (RuntimeException e) {
                log.error("Error while checking the deployment plan of {}", deployment, e);
            }
        });
    }

    private PlanDV determinePlan(int usersCount, int clientsCount, int groupsCount, int rolesCount) {
        if (usersCount < PlanDV.BEGINNER.getUsersCount() && clientsCount < PlanDV.BEGINNER.getClientsCount() && groupsCount < PlanDV.BEGINNER.getGroupsCount() && rolesCount < PlanDV.BEGINNER.getRolesCount()) {
            return PlanDV.BEGINNER;
        } else if (usersCount < PlanDV.NORMAL.getUsersCount() && clientsCount < PlanDV.NORMAL.getClientsCount() && groupsCount < PlanDV.NORMAL.getGroupsCount() && rolesCount < PlanDV.NORMAL.getRolesCount()) {
            return PlanDV.NORMAL;
        } else if (usersCount < PlanDV.ADVANCED.getUsersCount() && clientsCount < PlanDV.ADVANCED.getClientsCount() && groupsCount < PlanDV.ADVANCED.getGroupsCount() && rolesCount < PlanDV.ADVANCED.getRolesCount()) {
            return PlanDV.ADVANCED;
        } else if (usersCount < PlanDV.PROFESSIONAL.getUsersCount() && clientsCount < PlanDV.PROFESSIONAL.getClientsCount() && groupsCount < PlanDV.PROFESSIONAL.getGroupsCount() && rolesCount < PlanDV.PROFESSIONAL.getRolesCount()) {
            return PlanDV.PROFESSIONAL;
        } else {
            return PlanDV.IMAGINARY;
        }
    }
}

package com.omidmk.iamapi.scheduler;

import com.omidmk.iamapi.exception.RealmNotFoundException;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
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
public class CostCalculatorScheduler {
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
    public void calculate() {
        Page<DeploymentModel> allActiveDeployments = deploymentService.findAllActiveDeployments(Pageable.unpaged());
        allActiveDeployments.forEach(deployment -> {
            float costPerInterval = interval.getSeconds() * ((float) deployment.getPlan().getCostPerHour() / (60 * 60));
            UserModel user = deployment.getUser();
            user.setBalance((long) (user.getBalance() - (costPerInterval * factor)));
            customerService.saveUser(user);
            if (user.getBalance() <= 0) {
                log.info("Deployment with realm name {} of user {} is going to be disabled. Current balance: {}", deployment.getRealmName(), user.getEmail(), user.getBalance());
                try {
                    keycloakService.disableRealm(deployment.getRealmName());
                } catch (RealmNotFoundException e) {
                    log.warn("Tried to delete the realm {} but not found!", deployment.getRealmName(), e);
                }
                // todo: find deployment of user properly (with user object)
                deployment.setState(DeploymentModel.State.STOPPED);
                deploymentService.saveDeployment(deployment);
            }
        });
    }
}

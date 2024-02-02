package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.config.KeycloakProperties;
import com.omidmk.iamapi.controller.dto.*;
import com.omidmk.iamapi.exception.*;
import com.omidmk.iamapi.mapper.DeploymentMapper;
import com.omidmk.iamapi.mapper.TicketMapper;
import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static com.omidmk.iamapi.config.SwaggerConfiguration.BEARER_TOKEN_SECURITY_SCHEME;

@RestController
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CustomerController {
    private final DeploymentService deploymentService;
    private final KeycloakService keycloakService;
    private final MailService mailService;
    private final TicketService ticketService;

    private final KeycloakProperties keycloakProperties;
    private final DeploymentMapper deploymentMapper;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;

    @Value("${app.iam-aas.fail-on-mail-error:false}")
    private boolean failOnMailError;

    @PostMapping("/deployments")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Deployment createDeployment(@AuthenticationPrincipal UserModel user, @RequestBody @Valid CreateDeploymentDTO requestBody) throws ApplicationException {
        if (user.getBalance() <= 0)
            throw new BalanceNotEnoughException();

        if (!isRealmAvailable(requestBody.getRealmName()))
            throw new RealmAlreadyExistException();

        var deployment = new DeploymentModel();
        try {
            deployment = deploymentService.createDeployment(user, requestBody.getRealmName(), requestBody.getPlan());
            final var passwordBytes = new byte[16];
            new SecureRandom().nextBytes(passwordBytes);
            final var username = "admin";
            final var password = new String(Base64.getEncoder().encode(passwordBytes));
            final var realmUrl = "%s/admin/%s/console".formatted(keycloakProperties.getBaseUrl(), requestBody.getRealmName());
            keycloakService.createRealm(requestBody.getRealmName());
            keycloakService.createAdminUser(requestBody.getRealmName(), username, password, true);
            mailService.sendCustomerCredentials(user.getEmail(), username, password, realmUrl);
            deployment.setState(DeploymentModel.State.DEPLOYED);
        } catch (SendingMailFailedException ex) {
            log.error("Sending mail error occurred on creating deployment. {}", ex.getMessage(), ex);
            if (failOnMailError) {
                log.warn("Fail on mail error is on.");
                throw ex;
            }
            deployment.setState(DeploymentModel.State.DEPLOYED);
        } catch (ApplicationException ex) {
            log.error("Application Error occurred on creating deployment. {}", ex.getMessage(), ex);
            if (!(ex instanceof RealmAlreadyExistException)) {
                deployment.setState(DeploymentModel.State.FAILED_TO_DEPLOY);
                keycloakService.deleteRealm(requestBody.getRealmName());
            }
            throw ex;
        } catch (RuntimeException ex) {
            log.error("Runtime Error occurred on creating deployment. {}", ex.getMessage(), ex);
            deployment.setState(DeploymentModel.State.FAILED_TO_DEPLOY);
            keycloakService.deleteRealm(requestBody.getRealmName());
            throw new InternalException(ex.getMessage());
        } finally {
            deployment = deploymentService.saveDeployment(deployment);
        }
        return deploymentMapper.deploymentModelToDeployment(deployment);
    }

    @GetMapping("/deployments")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Deployment> getDeployments(@AuthenticationPrincipal UserModel user, @PageableDefault Pageable pageable) {
        Page<DeploymentModel> deployments = deploymentService.findDeploymentsOfUser(user, pageable);
        return deploymentMapper.deploymentModelListToDeploymentList(deployments.toList());
    }

    @GetMapping("/deployments/{deploymentId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Deployment getSingleDeployment(@AuthenticationPrincipal UserModel user, @PathVariable UUID deploymentId) throws ApplicationException {
        DeploymentModel deployment = deploymentService.findDeploymentOfUser(user, deploymentId);

        return deploymentMapper.deploymentModelToDeployment(deployment);
    }

    @PutMapping("/deployments/{deploymentId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Deployment updateDeployment(@AuthenticationPrincipal UserModel user, @PathVariable UUID deploymentId, @RequestBody @Valid UpdateDeploymentDTO updateDeploymentRequest) throws ApplicationException{
        DeploymentModel oldDeployment = deploymentService.findDeploymentOfUser(user, deploymentId);

        oldDeployment.setPlan(updateDeploymentRequest.getPlan());
        return deploymentMapper.deploymentModelToDeployment(deploymentService.saveDeployment(oldDeployment));
    }

    @DeleteMapping("/deployments/{deploymentId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public void deleteDeployment(@AuthenticationPrincipal UserModel user, @PathVariable UUID deploymentId) throws ApplicationException {
        DeploymentModel deployment = deploymentService.findDeploymentOfUser(user, deploymentId);

        deploymentService.deleteDeployment(deployment);
        keycloakService.deleteRealm(deployment.getRealmName());
    }

    @GetMapping("/deployments/available")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public boolean isRealmAvailable(@QueryParam("realmName") String realmName) {
        return deploymentService.isRealmAvailable(realmName) && keycloakService.isRealmAvailable(realmName);
    }

    @GetMapping("/tickets")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Ticket> getAllTickets(@AuthenticationPrincipal UserModel user, @PageableDefault Pageable pageable) {
        Page<TicketModel> allTickets = ticketService.findAllTicketsOfUser(user, pageable);
        return ticketMapper.ticketModelListToTicketList(allTickets.toList());
    }

    @GetMapping("/tickets/{ticketId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Ticket getSingleTicket(@AuthenticationPrincipal UserModel user, @PathVariable UUID ticketId) throws ApplicationException {
        TicketModel userTicket = ticketService.findUserTicketById(user, ticketId);

        return ticketMapper.ticketModelToTicket(userTicket);
    }

    @PostMapping("/tickets")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Ticket createNewTicket(@AuthenticationPrincipal UserModel user, @RequestBody @Valid AddTicketDialogRequest dialogRequest) {
        var ticket = new TicketModel();
        ticket.setCustomer(user);
        ticket.setState(TicketModel.State.WAITING_FOR_ADMIN_RESPONSE);
        var dialog = new DialogModel(user, dialogRequest.getDialog());
        ticket.setDialogs(List.of(dialog));
        ticket = ticketService.saveTicket(ticket);
        return ticketMapper.ticketModelToTicket(ticket);
    }

    @PostMapping("/tickets/{ticketId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Ticket addDialogToTicket(@AuthenticationPrincipal UserModel user, @PathVariable UUID ticketId, @RequestBody @Valid AddTicketDialogRequest dialogRequest) throws ApplicationException {
        TicketModel ticketModel = ticketService.findUserTicketById(user, ticketId);
        if (TicketModel.State.CLOSED.equals(ticketModel.getState()))
            throw new ClosedTicketModifyingException();

        var dialog = new DialogModel(user, dialogRequest.getDialog());
        TicketModel ticket = ticketService.findTicketById(ticketId);
        ticket.getDialogs().add(dialog);
        ticket.setState(TicketModel.State.WAITING_FOR_ADMIN_RESPONSE);
        ticket = ticketService.saveTicket(ticket);
        return ticketMapper.ticketModelToTicket(ticket);
    }

    @DeleteMapping("/tickets/{ticketId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public void deleteSingleTicket(@AuthenticationPrincipal UserModel user, @PathVariable UUID ticketId) throws ApplicationException {
        TicketModel ticket = ticketService.findUserTicketById(user, ticketId);
        ticketService.deleteTicket(ticket);
    }

    @GetMapping("tickets/{ticketId}/dialogs/{dialogId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Dialog getDialogOfTicket(@AuthenticationPrincipal UserModel user, @PathVariable UUID ticketId, @PathVariable UUID dialogId) throws ApplicationException {
        TicketModel ticket = ticketService.findUserTicketById(user, ticketId);

        DialogModel dialogModel = ticket.getDialogs()
                .stream()
                .filter(dialog -> dialog.getId().equals(dialogId))
                .findFirst()
                .orElseThrow(DialogNotFoundException::new);

        return ticketMapper.dialogModelToDialog(dialogModel);
    }

    @GetMapping("/userinfo")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Customer getUserInfo(@AuthenticationPrincipal UserModel user) {
        return userMapper.userModelToCustomer(user);
    }
}

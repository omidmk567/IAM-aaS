package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.config.KeycloakProperties;
import com.omidmk.iamapi.controller.dto.*;
import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.DialogNotFoundException;
import com.omidmk.iamapi.exception.TicketNotFoundException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.mapper.DeploymentMapper;
import com.omidmk.iamapi.mapper.TicketMapper;
import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import com.omidmk.iamapi.service.*;
import jakarta.ws.rs.QueryParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final DeploymentService deploymentService;
    private final KeycloakService keycloakService;
    private final MailService mailService;
    private final TicketService ticketService;
    private final CustomerService customerService;

    private final KeycloakProperties keycloakProperties;
    private final DeploymentMapper deploymentMapper;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;

    @PostMapping("/deployments")
    @ResponseStatus(HttpStatus.CREATED)
    public Deployment createDeployment(@AuthenticationPrincipal IAMUser user, @RequestBody CreateDeploymentDTO requestBody) throws ApplicationException {
        // todo: check for available balance
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
            // todo: save to database
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

    @DeleteMapping("/deployments/{deploymentId}")
    public void deleteDeployment(@AuthenticationPrincipal IAMUser user, @PathVariable UUID deploymentId) {
        // todo
    }

    @GetMapping("/deployments/available")
    public boolean isRealmAvailable(@QueryParam("realmName") String realmName) {
        return deploymentService.isRealmAvailable(realmName);
    }

    @GetMapping("/tickets")
    public List<Ticket> getAllTickets(@AuthenticationPrincipal IAMUser user) throws ApplicationException {
        List<TicketModel> allTickets = ticketService.findAllTicketsByUserId(user.getId());
        return ticketMapper.ticketModelListToTicketList(allTickets);
    }

    @GetMapping("/tickets/{ticketId}")
    public Ticket getSingleTicket(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId) throws ApplicationException {
        Optional<TicketModel> userTicket = ticketService.findUserTicketById(user.getId(), ticketId);
        if (userTicket.isEmpty())
            throw new TicketNotFoundException();

        return ticketMapper.ticketModelToTicket(userTicket.get());
    }

    @PostMapping("/tickets")
    public Ticket createNewTicket(@AuthenticationPrincipal IAMUser user, @RequestBody AddTicketDialogRequest dialogRequest) throws UserNotFoundException {
        UserModel userModel = userMapper.iamUserToUserModel(user);
        var dialog = new DialogModel(userModel, dialogRequest.getDialog());
        var ticket = new TicketModel();
        ticket.setCustomer(userModel);
        ticket.setState(TicketModel.State.WAITING_FOR_ADMIN_RESPONSE);
        ticket.setDialogs(List.of(dialog));
        userModel.getTickets().add(ticket);
        customerService.saveUser(userModel);
        return ticketMapper.ticketModelToTicket(ticket);
    }

    @PostMapping("/tickets/{ticketId}")
    public Ticket addDialogToTicket(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId, @RequestBody AddTicketDialogRequest dialogRequest) throws ApplicationException {
        Optional<TicketModel> ticket = ticketService.findUserTicketById(user.getId(), ticketId);
        if (ticket.isEmpty())
            throw new TicketNotFoundException();

        UserModel userModel = userMapper.iamUserToUserModel(user);
        var dialog = new DialogModel(userModel, dialogRequest.getDialog());
        ticket.get().getDialogs().add(dialog);
        ticket.get().setState(TicketModel.State.WAITING_FOR_ADMIN_RESPONSE);
        return ticketMapper.ticketModelToTicket(ticketService.saveTicket(ticket.get()));
    }

    @DeleteMapping("/tickets/{ticketId}")
    public void deleteSingleTicket(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId) throws ApplicationException {
        Optional<TicketModel> ticket = ticketService.findUserTicketById(user.getId(), ticketId);
        if (ticket.isEmpty())
            throw new TicketNotFoundException();

        ticketService.deleteTicket(ticketId);
    }

    @GetMapping("tickets/{ticketId}/dialogs")
    public List<Dialog> getAllDialogsOfTicket(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId) throws ApplicationException {
        Optional<TicketModel> ticket = ticketService.findUserTicketById(user.getId(), ticketId);
        if (ticket.isEmpty())
            throw new TicketNotFoundException();

        return ticketMapper.dialogModelListToDialogList(ticket.get().getDialogs());
    }

    @GetMapping("tickets/{ticketId}/dialogs/{dialogId}")
    public Dialog getDialogOfTicket(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId, @PathVariable UUID dialogId) throws ApplicationException {
        Optional<TicketModel> ticket = ticketService.findUserTicketById(user.getId(), ticketId);
        if (ticket.isEmpty())
            throw new TicketNotFoundException();

        DialogModel dialogModel = ticket.get().getDialogs()
                .stream()
                .filter(dialog -> dialog.getId().equals(dialogId))
                .findFirst()
                .orElseThrow(DialogNotFoundException::new);

        return ticketMapper.dialogModelToDialog(dialogModel);
    }

    @DeleteMapping("ticket/{ticketId}/dialogs/{dialogId}")
    public void deleteSingleDialog(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId, @PathVariable UUID dialogId) throws ApplicationException {
        Optional<TicketModel> ticket = ticketService.findUserTicketById(user.getId(), ticketId);
        if (ticket.isEmpty())
            throw new TicketNotFoundException();

        DialogModel dialogModel = ticket.get().getDialogs()
                .stream()
                .filter(dialog -> dialog.getId().equals(dialogId))
                .findFirst()
                .orElseThrow(DialogNotFoundException::new);

        ticketService.deleteDialog(dialogId);
    }

    @GetMapping("/userinfo")
    public Customer getUserInfo(@AuthenticationPrincipal IAMUser user) {
        return userMapper.iamUserToCustomer(user);
    }
}

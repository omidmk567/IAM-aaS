package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.controller.dto.*;
import com.omidmk.iamapi.exception.*;
import com.omidmk.iamapi.mapper.DeploymentMapper;
import com.omidmk.iamapi.mapper.TicketMapper;
import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.deployment.DeploymentModel;
import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.service.CustomerService;
import com.omidmk.iamapi.service.DeploymentService;
import com.omidmk.iamapi.service.KeycloakService;
import com.omidmk.iamapi.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.omidmk.iamapi.config.SwaggerConfiguration.BEARER_TOKEN_SECURITY_SCHEME;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {
    private final CustomerService customerService;
    private final TicketService ticketService;
    private final KeycloakService keycloakService;
    private final DeploymentService deploymentService;

    private final UserMapper userMapper;
    private final DeploymentMapper deploymentMapper;
    private final TicketMapper ticketMapper;

    @GetMapping("/customers")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Customer> getAllCustomers(@RequestParam(value = "filter", defaultValue = "customers") String filter, @PageableDefault Pageable pageable) {
        Page<UserModel> userModels = switch (filter) {
            case "customers" -> customerService.findAllCustomers(pageable);
            case "admins" -> customerService.findAllAdmins(pageable);
            case "all" -> customerService.findAll(pageable);
            default -> customerService.findAllCustomers(pageable);
        };
        return userMapper.userModelListToCustomerList(userModels.toList());
    }

    @GetMapping("/customers/{userId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Customer getSingleCustomer(@PathVariable UUID userId) throws UserNotFoundException {
        UserModel foundUser = customerService.findUserById(userId);

        return userMapper.userModelToCustomer(foundUser);
    }

    @PutMapping("/customers/{userId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Customer updateCustomer(@PathVariable UUID userId, @RequestBody @Valid UpdateCustomerDTO updateCustomerDTO) throws UserNotFoundException {
        if (updateCustomerDTO == null || updateCustomerDTO.getId() == null || !updateCustomerDTO.getId().equals(userId.toString()))
            throw new UserNotFoundException();

        UserModel userModel = customerService.findUserById(userId);
        userModel.setFirstName(updateCustomerDTO.getFirstName());
        userModel.setLastName(updateCustomerDTO.getLastName());
        userModel.setBalance(updateCustomerDTO.getBalance());

        return userMapper.userModelToCustomer(customerService.saveUser(userModel));
    }

    @DeleteMapping("/customers/{userId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public void deleteCustomer(@PathVariable UUID userId) throws UserNotFoundException, InternalException {
        UserModel userModel = customerService.findUserById(userId);
        var hasError = new AtomicBoolean(false);
        deploymentService.findDeploymentsOfUser(userModel, Pageable.unpaged()).forEach(deployment -> {
            try {
                keycloakService.deleteRealm(deployment.getRealmName());
            } catch (RealmNotFoundException e) {
                log.warn("Tried to delete realm but not found in keycloak!.", e);
            } catch (RuntimeException e) {
                log.error("Error occurred on deleting realm {} of user {}", deployment.getRealmName(), userId, e);
                hasError.set(true);
            }
        });
        if (hasError.get())
            throw new InternalException("Action not completed. See the logs for further information");

        customerService.deleteUser(userModel);
    }

    @GetMapping("/deployments")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Deployment> getAllDeployments(@PageableDefault Pageable pageable) {
        Page<DeploymentModel> allDeployments = deploymentService.findAllDeployments(pageable);

        return deploymentMapper.deploymentModelListToDeploymentList(allDeployments.toList());
    }

    @GetMapping("/deployments/active")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Deployment> getActiveDeployments(@PageableDefault Pageable pageable) {
        Page<DeploymentModel> activeDeployments = deploymentService.findAllActiveDeployments(pageable);

        return deploymentMapper.deploymentModelListToDeploymentList(activeDeployments.toList());
    }

    @GetMapping("/deployments/assigned")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Deployment> getAssignedDeployments(@PageableDefault Pageable pageable) {
        Page<DeploymentModel> assignedDeployments = deploymentService.findAllAssignedDeployments(pageable);

        return deploymentMapper.deploymentModelListToDeploymentList(assignedDeployments.toList());
    }

    @GetMapping("/deployments")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Deployment> getCustomerDeployments(@RequestParam UUID userId, @PageableDefault Pageable pageable) throws UserNotFoundException {
        UserModel user = customerService.findUserById(userId);
        Page<DeploymentModel> deploymentsOfUser = deploymentService.findDeploymentsOfUser(user, pageable);

        return deploymentMapper.deploymentModelListToDeploymentList(deploymentsOfUser.toList());
    }

    @GetMapping("/deployments/{deploymentId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Deployment getSingleDeployment(@PathVariable UUID deploymentId) throws DeploymentNotFoundException {
        DeploymentModel deploymentModel = deploymentService.findDeploymentById(deploymentId);

        return deploymentMapper.deploymentModelToDeployment(deploymentModel);
    }

    @PutMapping("/deployments/{deploymentId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Deployment updateDeployment(@PathVariable UUID deploymentId, @RequestBody @Valid UpdateDeploymentDTO updateDeploymentDTO) throws DeploymentNotFoundException {
        DeploymentModel deploymentModel = deploymentService.findDeploymentById(deploymentId);
        deploymentModel.setPlan(updateDeploymentDTO.getPlan());
        deploymentModel.setState(updateDeploymentDTO.getState());

        return deploymentMapper.deploymentModelToDeployment(deploymentService.saveDeployment(deploymentModel));
    }

    @DeleteMapping("/deployments/{deploymentId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public void deleteDeployment(@PathVariable UUID deploymentId) throws DeploymentNotFoundException {
        DeploymentModel deploymentModel = deploymentService.findDeploymentById(deploymentId);

        deploymentService.deleteDeployment(deploymentModel);
    }

    @GetMapping("/tickets")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Ticket> getAllTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> allTickets = ticketService.findAllTickets(pageable);

        return ticketMapper.ticketModelListToTicketList(allTickets.toList());
    }

    @GetMapping("/tickets/customers/{userId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Ticket> getUserTickets(@PathVariable UUID userId, @PageableDefault Pageable pageable) throws ApplicationException {
        UserModel user = customerService.findUserById(userId);
        Page<TicketModel> allTickets = ticketService.findAllTicketsOfUser(user, pageable);

        return ticketMapper.ticketModelListToTicketList(allTickets.toList());
    }

    @GetMapping("/tickets/{ticketId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Ticket getSingleTicket(@PathVariable UUID ticketId) throws TicketNotFoundException {
        TicketModel userTicket = ticketService.findTicketById(ticketId);

        return ticketMapper.ticketModelToTicket(userTicket);
    }

    @GetMapping("/tickets/unread")
    public List<Ticket> getUnreadTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> userTicket = ticketService.findWaitingForAdminTickets(pageable);

        return ticketMapper.ticketModelListToTicketList(userTicket.toList());
    }

    @GetMapping("/tickets/read")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Ticket> getRespondedTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> userTicket = ticketService.findWaitingForCustomerTickets(pageable);

        return ticketMapper.ticketModelListToTicketList(userTicket.toList());
    }

    @GetMapping("/tickets/closed")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Ticket> getClosedTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> userTicket = ticketService.findClosedTickets(pageable);

        return ticketMapper.ticketModelListToTicketList(userTicket.toList());
    }

    @PostMapping("/tickets/{ticketId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Ticket addDialogToTicket(@AuthenticationPrincipal UserModel adminUser, @PathVariable UUID ticketId, @RequestBody @Valid AdminAddTicketDialogRequest dialogRequest) throws TicketNotFoundException, ClosedTicketModifyingException {
        TicketModel ticketModel = ticketService.findTicketById(ticketId);
        if (TicketModel.State.CLOSED.equals(ticketModel.getState()))
            throw new ClosedTicketModifyingException();

        var dialog = new DialogModel(adminUser, dialogRequest.getDialog());
        ticketModel.getDialogs().add(dialog);
        ticketModel.setState(dialogRequest.getClose() ? TicketModel.State.CLOSED : TicketModel.State.WAITING_FOR_CUSTOMER_RESPONSE);
        ticketModel = ticketService.saveTicket(ticketModel);

        return ticketMapper.ticketModelToTicket(ticketModel);
    }
}

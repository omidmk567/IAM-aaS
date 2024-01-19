package com.omidmk.iamapi.controller;

import com.omidmk.iamapi.controller.dto.AdminAddTicketDialogRequest;
import com.omidmk.iamapi.controller.dto.Customer;
import com.omidmk.iamapi.controller.dto.Ticket;
import com.omidmk.iamapi.controller.dto.UpdateCustomerDTO;
import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.TicketNotFoundException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.mapper.TicketMapper;
import com.omidmk.iamapi.mapper.UserMapper;
import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.oauth2.model.IAMUser;
import com.omidmk.iamapi.service.CustomerService;
import com.omidmk.iamapi.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.omidmk.iamapi.config.SwaggerConfig.BEARER_TOKEN_SECURITY_SCHEME;

@RestController
@RequestMapping("/v1/admin")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {
    private final CustomerService customerService;
    private final TicketService ticketService;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;

    @GetMapping("/customers")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public List<Customer> getAllCustomers(@PageableDefault Pageable pageable) {
        return userMapper.userModelListToCustomerList(customerService.findAll(pageable).toList());
    }

    @GetMapping("/customers/{userId}")
    @Operation(security = {@SecurityRequirement(name = BEARER_TOKEN_SECURITY_SCHEME)})
    public Customer getSingleCustomer(@PathVariable UUID userId) throws ApplicationException {
        Optional<UserModel> foundUser = customerService.findUserById(userId);
        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        return userMapper.userModelToCustomer(foundUser.get());
    }

    @PutMapping("/customers/{userId}")
    public Customer updateCustomer(@PathVariable UUID userId, @RequestBody @Valid UpdateCustomerDTO updateCustomerDTO) throws ApplicationException {
        if (updateCustomerDTO == null || updateCustomerDTO.getId() == null || !updateCustomerDTO.getId().equals(userId))
            throw new UserNotFoundException();

        Optional<UserModel> foundUser = customerService.findUserById(userId);
        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        UserModel userModel = foundUser.get();
        if (StringUtils.isNotEmpty(updateCustomerDTO.getFirstName()))
            userModel.setFirstName(updateCustomerDTO.getFirstName());

        if (StringUtils.isNotEmpty(updateCustomerDTO.getLastName()))
            userModel.setLastName(updateCustomerDTO.getLastName());

        if (updateCustomerDTO.getBalance() != null)
            userModel.setBalance(updateCustomerDTO.getBalance());
        return userMapper.userModelToCustomer(customerService.saveUser(userModel));
    }

    @DeleteMapping("/customers/{userId}")
    public void deleteCustomer(@PathVariable UUID userId) throws ApplicationException {
        Optional<UserModel> foundUser = customerService.findUserById(userId);
        if (foundUser.isEmpty())
            throw new UserNotFoundException();

        customerService.deleteUserById(foundUser.get().getId());
    }

    @GetMapping("/tickets")
    public List<Ticket> getAllTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> allTickets = ticketService.findAllTickets(pageable);
        return ticketMapper.ticketModelListToTicketList(allTickets.toList());
    }

    @GetMapping("/tickets/customers/{userId}")
    public List<Ticket> getUserTickets(@PathVariable UUID userId, @PageableDefault Pageable pageable) throws ApplicationException {
        Page<TicketModel> allTickets = ticketService.findAllTicketsByUserId(userId, pageable);
        return ticketMapper.ticketModelListToTicketList(allTickets.toList());
    }

    @GetMapping("/tickets/{ticketId}")
    public Ticket getSingleTicket(@PathVariable UUID ticketId) throws ApplicationException {
        Optional<TicketModel> userTicket = ticketService.findTicketById(ticketId);
        if (userTicket.isEmpty())
            throw new TicketNotFoundException();

        return ticketMapper.ticketModelToTicket(userTicket.get());
    }

    @GetMapping("/tickets/unread")
    public List<Ticket> getUnreadTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> userTicket = ticketService.findWaitingForAdminTickets(pageable);
        return ticketMapper.ticketModelListToTicketList(userTicket.toList());
    }

    @GetMapping("/tickets/read")
    public List<Ticket> getRespondedTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> userTicket = ticketService.findWaitingForCustomerTickets(pageable);
        return ticketMapper.ticketModelListToTicketList(userTicket.toList());
    }

    @GetMapping("/tickets/closed")
    public List<Ticket> getClosedTickets(@PageableDefault Pageable pageable) {
        Page<TicketModel> userTicket = ticketService.findClosedTickets(pageable);
        return ticketMapper.ticketModelListToTicketList(userTicket.toList());
    }

    @PostMapping("/tickets/{ticketId}")
    public Ticket addDialogToTicket(@AuthenticationPrincipal IAMUser user, @PathVariable UUID ticketId, @RequestBody @Valid AdminAddTicketDialogRequest dialogRequest) throws ApplicationException {
        Optional<TicketModel> ticket = ticketService.findTicketById(ticketId);
        if (ticket.isEmpty())
            throw new TicketNotFoundException();

        UserModel adminUserModel = userMapper.iamUserToUserModel(user);
        var dialog = new DialogModel(adminUserModel, dialogRequest.getDialog());
        ticket.get().getDialogs().add(dialog);
        ticket.get().setState(dialogRequest.getClose() ? TicketModel.State.CLOSED : TicketModel.State.WAITING_FOR_CUSTOMER_RESPONSE);
        return ticketMapper.ticketModelToTicket(ticketService.saveTicket(ticket.get()));
    }
}

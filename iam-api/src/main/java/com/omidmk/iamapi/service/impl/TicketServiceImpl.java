package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.exception.UserNotFoundException;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.DialogRepository;
import com.omidmk.iamapi.repository.TicketRepository;
import com.omidmk.iamapi.service.CustomerService;
import com.omidmk.iamapi.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final DialogRepository dialogRepository;
    private final CustomerService customerService;

    @Override
    public Page<TicketModel> findAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Page<TicketModel> findAllTicketsByUserId(UUID userId, Pageable pageable) throws ApplicationException {
        Optional<UserModel> user = customerService.findUserById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException();

        return ticketRepository.findAllByCustomerIs(user.get(), pageable);
    }

    @Override
    public Optional<TicketModel> findUserTicketById(UUID userId, UUID ticketId) throws ApplicationException {
        Optional<UserModel> user = customerService.findUserById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException();

        return ticketRepository.findByIdAndCustomer(ticketId, user.get());
    }

    @Override
    public Page<TicketModel> findWaitingForAdminTickets(Pageable pageable) {
        return ticketRepository.findAllByStateIs(TicketModel.State.WAITING_FOR_ADMIN_RESPONSE, pageable);
    }

    @Override
    public Page<TicketModel> findWaitingForCustomerTickets(Pageable pageable) {
        return ticketRepository.findAllByStateIs(TicketModel.State.WAITING_FOR_CUSTOMER_RESPONSE, pageable);
    }

    @Override
    public Page<TicketModel> findClosedTickets(Pageable pageable) {
        return ticketRepository.findAllByStateIs(TicketModel.State.CLOSED, pageable);
    }

    @Override
    public Optional<TicketModel> findTicketById(UUID ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public TicketModel saveTicket(TicketModel ticketModel) {
        return ticketRepository.save(ticketModel);
    }

    @Override
    public void deleteTicket(UUID ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public void deleteDialog(UUID dialogId) {
        dialogRepository.deleteById(dialogId);
    }
}

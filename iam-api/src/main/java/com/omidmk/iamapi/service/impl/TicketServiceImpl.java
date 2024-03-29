package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.exception.TicketNotFoundException;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import com.omidmk.iamapi.repository.DialogRepository;
import com.omidmk.iamapi.repository.TicketRepository;
import com.omidmk.iamapi.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final DialogRepository dialogRepository;

    @Override
    public Page<TicketModel> findAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Page<TicketModel> findAllTicketsOfUser(UserModel userModel, Pageable pageable) {
        return ticketRepository.findAllByCustomerIs(userModel, pageable);
    }

    @Override
    public TicketModel findUserTicketById(UserModel userModel, UUID ticketId) throws TicketNotFoundException {
        return ticketRepository.findByIdAndCustomer(ticketId, userModel).orElseThrow(TicketNotFoundException::new);
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
    public TicketModel findTicketById(UUID ticketId) throws TicketNotFoundException {
        return ticketRepository.findById(ticketId).orElseThrow(TicketNotFoundException::new);
    }

    public TicketModel saveTicket(TicketModel ticketModel) {
        return ticketRepository.save(ticketModel);
    }

    @Override
    public void deleteTicket(UUID ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public void deleteTicket(TicketModel ticketModel) {
        ticketRepository.delete(ticketModel);
    }

    @Override
    public void deleteDialog(UUID dialogId) {
        dialogRepository.deleteById(dialogId);
    }
}

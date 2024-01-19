package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.model.ticket.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    Page<TicketModel> findAllTickets(Pageable pageable);

    Page<TicketModel> findAllTicketsByUserId(UUID userId, Pageable pageable) throws ApplicationException;

    Optional<TicketModel> findUserTicketById(UUID userId, UUID ticketId) throws ApplicationException;

    Page<TicketModel> findWaitingForAdminTickets(Pageable pageable);

    Page<TicketModel> findWaitingForCustomerTickets(Pageable pageable);

    Page<TicketModel> findClosedTickets(Pageable pageable);

    Optional<TicketModel> findTicketById(UUID ticketId);

    TicketModel saveTicket(TicketModel ticketModel);

    void deleteTicket(UUID ticketId);

    void deleteDialog(UUID dialogId);
}

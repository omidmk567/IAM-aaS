package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.ApplicationException;
import com.omidmk.iamapi.model.ticket.TicketModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketService {
    List<TicketModel> findAllTicketsByUserId(UUID userId) throws ApplicationException;

    Optional<TicketModel> findUserTicketById(UUID userId, UUID ticketId) throws ApplicationException;

    TicketModel saveTicket(TicketModel ticketModel);

    void deleteTicket(UUID ticketId);

    void deleteDialog(UUID dialogId);
}

package com.omidmk.iamapi.service;

import com.omidmk.iamapi.exception.TicketNotFoundException;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TicketService {
    Page<TicketModel> findAllTickets(Pageable pageable);

    Page<TicketModel> findAllTicketsOfUser(UserModel userModel, Pageable pageable);

    TicketModel findUserTicketById(UserModel userModel, UUID ticketId) throws TicketNotFoundException;

    Page<TicketModel> findWaitingForAdminTickets(Pageable pageable);

    Page<TicketModel> findWaitingForCustomerTickets(Pageable pageable);

    Page<TicketModel> findClosedTickets(Pageable pageable);

    TicketModel findTicketById(UUID ticketId) throws TicketNotFoundException;

    TicketModel saveTicket(TicketModel ticketModel);

    void deleteTicket(UUID ticketId);

    void deleteTicket(TicketModel ticketModel);

    void deleteDialog(UUID dialogId);
}

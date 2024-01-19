package com.omidmk.iamapi.service;

import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DialogService {
    Page<DialogModel> findDialogsOfTicket(TicketModel ticketModel, Pageable pageable);
}

package com.omidmk.iamapi.controller.dto;

import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Ticket {
    private UUID id;
    private UUID customerId;
    private List<Dialog> dialogs;
    private TicketModel.State state;
}

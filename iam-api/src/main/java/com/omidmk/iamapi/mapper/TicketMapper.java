package com.omidmk.iamapi.mapper;

import com.omidmk.iamapi.controller.dto.Dialog;
import com.omidmk.iamapi.controller.dto.Ticket;
import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TicketMapper {
    @Mapping(source = "dialogModel.user.id", target = "userId")
    Dialog dialogModelToDialog(DialogModel dialogModel);

    List<Dialog> dialogModelListToDialogList(List<DialogModel> ticketModel);

    @Mapping(source = "ticketModel.customer.id", target = "customerId")
    Ticket ticketModelToTicket(TicketModel ticketModel);

    List<Ticket> ticketModelListToTicketList(List<TicketModel> ticketModel);
}

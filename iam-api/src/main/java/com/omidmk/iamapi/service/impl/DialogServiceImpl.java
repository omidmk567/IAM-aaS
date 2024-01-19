package com.omidmk.iamapi.service.impl;

import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.repository.DialogRepository;
import com.omidmk.iamapi.service.DialogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DialogServiceImpl implements DialogService {
    private final DialogRepository dialogRepository;

    @Override
    public Page<DialogModel> findDialogsOfTicket(TicketModel ticketModel, Pageable pageable) {
        return dialogRepository.findAllByTicketIs(ticketModel, pageable);
    }
}

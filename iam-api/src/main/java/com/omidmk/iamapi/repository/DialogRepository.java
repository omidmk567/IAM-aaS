package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.ticket.DialogModel;
import com.omidmk.iamapi.model.ticket.TicketModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DialogRepository extends JpaRepository<DialogModel, UUID> {
    Page<DialogModel> findAllByTicketIs(TicketModel ticket, Pageable pageable);
}

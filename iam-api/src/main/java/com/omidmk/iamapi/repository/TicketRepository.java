package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketModel, UUID> {
    Page<TicketModel> findAllByCustomerIs(UserModel customer, Pageable pageable);

    Optional<TicketModel> findByIdAndCustomer(UUID id, UserModel customer);

    Page<TicketModel> findAllByStateIs(TicketModel.State state, Pageable pageable);
}

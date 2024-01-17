package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.ticket.TicketModel;
import com.omidmk.iamapi.model.user.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends ListCrudRepository<TicketModel, UUID>, PagingAndSortingRepository<TicketModel, UUID> {
    List<TicketModel> findAllByCustomerIs(UserModel customer);
    Optional<TicketModel> findByIdAndCustomer(UUID id, UserModel customer);

    List<TicketModel> findAllByStateIs(TicketModel.State state, Pageable pageable);
}

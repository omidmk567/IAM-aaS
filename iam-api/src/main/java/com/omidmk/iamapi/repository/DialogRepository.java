package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.ticket.DialogModel;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DialogRepository extends ListCrudRepository<DialogModel, UUID>, PagingAndSortingRepository<DialogModel, UUID> {
}

package com.omidmk.iamapi.repository;

import com.omidmk.iamapi.model.ticket.DialogModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DialogRepository extends JpaRepository<DialogModel, UUID> {
}

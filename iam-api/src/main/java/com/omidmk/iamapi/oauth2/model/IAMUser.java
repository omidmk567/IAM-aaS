package com.omidmk.iamapi.oauth2.model;

import com.omidmk.iamapi.controller.dto.Deployment;
import com.omidmk.iamapi.controller.dto.Ticket;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class IAMUser {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Long balance;
    private List<Deployment> deployments;
    private List<Ticket> tickets;
    private Long version;
}

package com.omidmk.iamapi.controller.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Customer {
    private UUID id;
    private String email;
    private Long balance;
    private List<Deployment> deployments;
}

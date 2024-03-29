package com.omidmk.iamapi.controller.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class Customer {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Long balance;
}

package com.omidmk.iamapi.oauth2.model;

import lombok.Data;

import java.util.UUID;

@Data
public class IAMUser {
    private UUID id;
    private String email;
    private Boolean isAdmin;
    private String firstName;
    private String lastName;
    private Long balance;
    private Long version;
}

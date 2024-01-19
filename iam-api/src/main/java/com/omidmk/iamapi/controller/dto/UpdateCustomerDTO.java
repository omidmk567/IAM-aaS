package com.omidmk.iamapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCustomerDTO {
    @NotBlank
    private UUID id;
    private String firstName;
    private String lastName;
    private Long balance;
}

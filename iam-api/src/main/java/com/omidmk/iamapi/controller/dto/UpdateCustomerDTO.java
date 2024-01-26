package com.omidmk.iamapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCustomerDTO {
    @NotBlank
    private String id;
    private String firstName;
    private String lastName;
    private Long balance;
}

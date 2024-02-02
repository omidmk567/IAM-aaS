package com.omidmk.iamapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UpdateCustomerDTO {
    @NotBlank
    private String id;
    private String firstName;
    private String lastName;
    @PositiveOrZero
    private Float balance;
}

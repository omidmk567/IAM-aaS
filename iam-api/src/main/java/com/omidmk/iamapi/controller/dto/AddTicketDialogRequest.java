package com.omidmk.iamapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddTicketDialogRequest {
    @NotBlank
    private String dialog;
}

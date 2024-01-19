package com.omidmk.iamapi.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminAddTicketDialogRequest {
    @NotBlank
    private String dialog;

    @NotNull
    private Boolean close;
}

package com.omidmk.iamapi.controller.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Dialog {
    private UUID id;
    private UUID userId;
    private String text;
    private Instant createdAt;
}

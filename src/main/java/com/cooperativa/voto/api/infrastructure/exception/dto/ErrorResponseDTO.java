package com.cooperativa.voto.api.infrastructure.exception.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}

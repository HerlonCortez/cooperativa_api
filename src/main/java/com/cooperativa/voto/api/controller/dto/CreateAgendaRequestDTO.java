package com.cooperativa.voto.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateAgendaRequestDTO(
        @NotBlank(message = "O título da pauta é obrigatório.")
        String title,
        String description
) {}

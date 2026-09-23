package com.cooperativa.voto.api.controller.dto;

import jakarta.validation.constraints.NotNull;

public record OpenSessionRequestDTO(
        @NotNull(message = "O ID da pauta é obrigatório")
        Long agendaId,
        Integer timeInMinutes
) {
}

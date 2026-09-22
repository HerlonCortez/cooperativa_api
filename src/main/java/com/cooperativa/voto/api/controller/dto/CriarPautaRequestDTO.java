package com.cooperativa.voto.api.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
public record CriarPautaRequestDTO(
        @NotBlank(message = "O título da pauta é obrigatório.")
        String titulo,
        String descricao
) {}

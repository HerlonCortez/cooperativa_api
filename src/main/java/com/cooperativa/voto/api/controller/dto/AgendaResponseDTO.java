package com.cooperativa.voto.api.controller.dto;

import com.cooperativa.voto.api.domain.entity.Pauta;

public record AgendaResponseDTO(
        Long id,
        String titulo,
        String descricao
) {
    public static AgendaResponseDTO fromDomain(Pauta agenda) {
        return new AgendaResponseDTO(
                agenda.getId(),
                agenda.getTitulo(),
                agenda.getDescricao());
    }
}

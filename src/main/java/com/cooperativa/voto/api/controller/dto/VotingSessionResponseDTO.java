package com.cooperativa.voto.api.controller.dto;

import com.cooperativa.voto.api.domain.entity.SessaoVotacao;

import java.time.LocalDateTime;

public record VotingSessionResponseDTO(
        Long id,
        Long agendaId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean open
) {
    public static VotingSessionResponseDTO fromEntity(SessaoVotacao sessaoVotacao) {
        return new VotingSessionResponseDTO(
                sessaoVotacao.getId(),
                sessaoVotacao.getPauta().getId(),
                sessaoVotacao.getDataAbertura(),
                sessaoVotacao.getDataFechamento(),
                sessaoVotacao.isAberta()
        );
    }
}

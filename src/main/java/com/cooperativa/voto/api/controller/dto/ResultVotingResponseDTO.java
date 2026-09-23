package com.cooperativa.voto.api.controller.dto;

import com.cooperativa.voto.api.domain.dto.ResultVotingDTO;
import com.cooperativa.voto.api.domain.entity.SessaoVotacao;

public record ResultVotingResponseDTO(
        Long agendaId,
        String agendaTitle,
        long totalVotes,
        long yesVotes,
        long noVotes,
        String result
) {
    public static ResultVotingResponseDTO fromDomain(ResultVotingDTO result) {
        return new ResultVotingResponseDTO(
                result.agendaId(),
                result.agendaTitle(),
                result.totalVotes(),
                result.yesVotes(),
                result.noVotes(),
                result.result()
        );
    }
}

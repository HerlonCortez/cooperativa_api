package com.cooperativa.voto.api.domain.dto;

public record ResultVotingDTO(
        Long agendaId,
        String agendaTitle,
        long totalVotes,
        long yesVotes,
        long noVotes,
        String result
) {}

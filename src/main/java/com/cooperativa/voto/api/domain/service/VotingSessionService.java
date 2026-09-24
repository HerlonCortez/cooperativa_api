package com.cooperativa.voto.api.domain.service;

import com.cooperativa.voto.api.domain.entity.SessaoVotacao;

public interface VotingSessionService {
    SessaoVotacao openSession(Long agendaId, Integer durationInMinutes);
    SessaoVotacao getAgendaId(Long agendaId);
}

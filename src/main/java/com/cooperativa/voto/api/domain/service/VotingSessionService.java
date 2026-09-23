package com.cooperativa.voto.api.domain.service;

import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.domain.exception.RegraNegocioException;
import com.cooperativa.voto.api.infrastructure.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VotingSessionService {
    private final VotingSessionRepository votingSessionRepository;
    private final AgendaService agendaService;

    public SessaoVotacao openSession(Long agendaId, Integer durationInMinutes){
        var agenda = agendaService.getAgendaId(agendaId);

        if (votingSessionRepository.existsByPautaId(agenda.getId())) {
            throw new RegraNegocioException("Já existe uma sessão de votação criada para esta pauta.");
        }

        int duration = (durationInMinutes != null && durationInMinutes > 0) ? durationInMinutes : 1;

        var now = LocalDateTime.now();

        var session = SessaoVotacao.builder()
                .pauta(agenda)
                .dataAbertura(now)
                .dataFechamento(now.plusMinutes(duration))
                .build();

        return votingSessionRepository.save(session);
    }

    public SessaoVotacao getAgendaId(Long agendaId){
        return votingSessionRepository.findByPautaId(agendaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma sessão de votação encontrada para a pauta ID: " + agendaId));
    }
}


package com.cooperativa.voto.api.domain.service.impl;

import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.domain.exception.RegraNegocioException;
import com.cooperativa.voto.api.domain.service.AgendaService;
import com.cooperativa.voto.api.domain.service.VotingSessionService;
import com.cooperativa.voto.api.infrastructure.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingSessionServiceImpl implements VotingSessionService {
    private final VotingSessionRepository votingSessionRepository;
    private final AgendaService agendaServiceImpl;

    @Override
    public SessaoVotacao openSession(Long agendaId, Integer durationInMinutes){
        log.info("Iniciando a criação de uma sessão");

        log.info("Buscando uma pauta por id da pauta");
        var agenda = agendaServiceImpl.getAgendaId(agendaId);

        log.info("Verificando se já existe uma sessão criada");
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
        log.info("Registrando a criação de uma sessão");
        var newSession = votingSessionRepository.save(session);
        log.info("Sessão criada com sucesso");

        return newSession;
    }

    @Override
    public SessaoVotacao getAgendaId(Long agendaId){
        return votingSessionRepository.findByPautaId(agendaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Nenhuma sessão de votação encontrada para a pauta ID: " + agendaId));
    }
}


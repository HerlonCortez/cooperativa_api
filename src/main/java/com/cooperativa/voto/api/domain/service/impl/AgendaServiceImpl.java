package com.cooperativa.voto.api.domain.service.impl;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.domain.service.AgendaService;
import com.cooperativa.voto.api.infrastructure.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {
    private final AgendaRepository agendaRepository;

    @Override
    @Transactional
    public Pauta createAgenda(String title, String description) {
        log.info("Iniciando a criação de uma pauta");
        var agenda = Pauta.builder()
                .titulo(title)
                .descricao(description)
                .build();
        Pauta newAgenda = agendaRepository.save(agenda);
        log.info("Pauta criada com sucesso");
        return newAgenda;
    }

    @Override
    public Pauta getAgendaId(Long agendaId) {
        log.info("Iniciando a busca de uma pauta por ID");
        Pauta agenda = agendaRepository.findById(agendaId).orElseThrow(
                () -> new RecursoNaoEncontradoException("Pauta não encontrada com ID: " + agendaId)
        );
        log.info("Pauta encontrada para o id:"+agendaId);
        return agenda;
    }
}

package com.cooperativa.voto.api.domain.service;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.infrastructure.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaService {
    private final AgendaRepository agendaRepository;

    @Transactional
    public Pauta createAgenda(String title, String description) {
        var agenda = Pauta.builder()
                .titulo(title)
                .descricao(description)
                .build();

        return agendaRepository.save(agenda);
    }

    public Pauta getAgendaId(Long agendaId) {
        return agendaRepository.findById(agendaId).orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada com ID: " + agendaId));
    }
}

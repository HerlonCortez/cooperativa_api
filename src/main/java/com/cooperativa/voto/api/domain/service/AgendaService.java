package com.cooperativa.voto.api.domain.service;

import com.cooperativa.voto.api.domain.entity.Pauta;

public interface AgendaService {
    Pauta createAgenda(String title, String description);
    Pauta getAgendaId(Long agendaId);
}

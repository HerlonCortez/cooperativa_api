package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.api.AgendaController;
import com.cooperativa.voto.api.controller.dto.CreateAgendaRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.service.impl.AgendaServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgendaControllerImpl implements AgendaController {

    private final AgendaServiceImpl agendaServiceImpl;

    @Override
    public ResponseEntity<Pauta> createAgenda(@RequestBody @Valid CreateAgendaRequestDTO request) {
        var pauta = agendaServiceImpl.createAgenda(request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @Override
    public ResponseEntity<Pauta> getAgendaId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaServiceImpl.getAgendaId(id));
    }
}

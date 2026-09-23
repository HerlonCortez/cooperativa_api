package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.CreateAgendaRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.service.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pautas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    public ResponseEntity<Pauta> createAgenda(@RequestBody @Valid CreateAgendaRequestDTO request) {
        var pauta = agendaService.createAgenda(request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> getAgendaId(@PathVariable Long id) {
        return ResponseEntity.ok(agendaService.getAgendaId(id));
    }
}

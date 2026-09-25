package com.cooperativa.voto.api.controller.api;

import com.cooperativa.voto.api.controller.dto.CreateAgendaRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Agenda", description = "Operações relacionadas à agenda")
@RequestMapping("/v1/pautas")
public interface AgendaController {

    @PostMapping
    @Operation(summary = "Registra a criação de uma pauta.")
    ResponseEntity<Pauta> createAgenda(@RequestBody @Valid CreateAgendaRequestDTO request);

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma pauta por id.")
    ResponseEntity<Pauta> getAgendaId(@PathVariable Long id);
}

package com.cooperativa.voto.api.controller.api;

import com.cooperativa.voto.api.controller.dto.OpenSessionRequestDTO;
import com.cooperativa.voto.api.controller.dto.VotingSessionResponseDTO;
import com.cooperativa.voto.api.domain.service.impl.VotingSessionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Sessão", description = "Operações relacionadas à sessão")
@RequestMapping("/v1/sessoes")
public interface VotingSessionController {
    @PostMapping("/abrir")
    @Operation(summary = "Registra a abertura de uma sessão.")
    ResponseEntity<VotingSessionResponseDTO> openSession(@RequestBody @Valid OpenSessionRequestDTO request);

    @GetMapping("/pauta/{agendaId}")
    @Operation(summary = "Busca por uma pauta dentro da sessão.")
    ResponseEntity<VotingSessionResponseDTO> getAgenda(@PathVariable Long agendaId);
}

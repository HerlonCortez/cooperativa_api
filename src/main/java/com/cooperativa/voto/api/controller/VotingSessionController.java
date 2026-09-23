package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.OpenSessionRequestDTO;
import com.cooperativa.voto.api.controller.dto.VotingSessionResponseDTO;
import com.cooperativa.voto.api.domain.service.VotingSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sessoes")
public class VotingSessionController {
    private final VotingSessionService votingSessionService;

    @PostMapping("/abrir")
    public ResponseEntity<VotingSessionResponseDTO> openSession(@RequestBody @Valid OpenSessionRequestDTO request){
        var sessao = votingSessionService.openSession(request.agendaId(), request.timeInMinutes());
        return ResponseEntity.status(HttpStatus.CREATED).body(VotingSessionResponseDTO.fromEntity(sessao));
    }

    @GetMapping("/pauta/{agendaId}")
    public ResponseEntity<VotingSessionResponseDTO> getAgenda(@PathVariable Long agendaId){
        var session = votingSessionService.getAgendaId(agendaId);
        return ResponseEntity.status(HttpStatus.OK).body(VotingSessionResponseDTO.fromEntity(session));
    }
}

package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.api.VotingSessionController;
import com.cooperativa.voto.api.controller.dto.OpenSessionRequestDTO;
import com.cooperativa.voto.api.controller.dto.VotingSessionResponseDTO;
import com.cooperativa.voto.api.domain.service.impl.VotingSessionServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VotingSessionControllerImpl implements VotingSessionController {
    private final VotingSessionServiceImpl votingSessionServiceImpl;

    @Override
    public ResponseEntity<VotingSessionResponseDTO> openSession(@RequestBody @Valid OpenSessionRequestDTO request){
        var sessao = votingSessionServiceImpl.openSession(request.agendaId(), request.timeInMinutes());
        return ResponseEntity.status(HttpStatus.CREATED).body(VotingSessionResponseDTO.fromEntity(sessao));
    }

    @Override
    public ResponseEntity<VotingSessionResponseDTO> getAgenda(@PathVariable Long agendaId){
        var session = votingSessionServiceImpl.getAgendaId(agendaId);
        return ResponseEntity.status(HttpStatus.OK).body(VotingSessionResponseDTO.fromEntity(session));
    }
}

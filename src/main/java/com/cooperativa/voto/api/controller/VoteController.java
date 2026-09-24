package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.RecordVoteRequestDTO;
import com.cooperativa.voto.api.controller.dto.ResultVotingResponseDTO;
import com.cooperativa.voto.api.domain.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/votos")
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Map<String, String>> registrarVoto(@RequestBody @Valid RecordVoteRequestDTO request) {
        voteService.castVote(request.agendaId(), request.getCpf(), request.vote());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensagem", "Voto registrado com sucesso!"));
    }

    @GetMapping("/resultado/{agendaId}")
    public ResponseEntity<ResultVotingResponseDTO> countVotes(@PathVariable Long agendaId) {
        ResultVotingResponseDTO resultado = ResultVotingResponseDTO.fromDomain(voteService.countVotes(agendaId));
        return ResponseEntity.ok(resultado);
    }
}

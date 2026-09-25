package com.cooperativa.voto.api.controller.api;

import com.cooperativa.voto.api.controller.dto.RecordVoteRequestDTO;
import com.cooperativa.voto.api.controller.dto.ResultVotingResponseDTO;
import com.cooperativa.voto.api.domain.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Votação", description = "Operações relacionadas à votação")
@RequestMapping("/v1/votos")
public interface VoteController {

    @PostMapping
    @Operation(summary = "Registra um voto.")
    ResponseEntity<Map<String, String>> registrarVoto(@RequestBody @Valid RecordVoteRequestDTO request) ;

    @GetMapping("/resultado/{agendaId}")
    @Operation(summary = "Contabiliza os votos.")
    ResponseEntity<ResultVotingResponseDTO> countVotes(@PathVariable Long agendaId);
}

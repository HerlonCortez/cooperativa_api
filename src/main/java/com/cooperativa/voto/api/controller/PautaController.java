package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.CriarPautaRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.service.PautaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;

    @PostMapping
    public ResponseEntity<Pauta> criarPauta(@RequestBody @Valid CriarPautaRequestDTO request) {
        var pauta = pautaService.criarPauta(request.titulo(), request.descricao());
        return ResponseEntity.status(HttpStatus.CREATED).body(pauta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pautaService.buscarPorId(id));
    }
}

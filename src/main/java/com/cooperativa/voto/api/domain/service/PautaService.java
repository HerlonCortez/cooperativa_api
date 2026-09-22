package com.cooperativa.voto.api.domain.service;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.infrastructure.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PautaService {
    private final PautaRepository pautaRepository;

    @Transactional
    public Pauta criarPauta(String titulo, String descricao) {
        var pauta = Pauta.builder()
                .titulo(titulo)
                .descricao(descricao)
                .build();

        return pautaRepository.save(pauta);
    }

    public Pauta buscarPorId(Long pautaId) {
        return pautaRepository.findById(pautaId).orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada com ID: " + pautaId));
    }
}

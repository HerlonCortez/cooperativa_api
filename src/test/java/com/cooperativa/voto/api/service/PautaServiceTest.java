package com.cooperativa.voto.api.service;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.domain.service.PautaService;
import com.cooperativa.voto.api.infrastructure.repository.PautaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.AssertionErrors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceTest {
    @InjectMocks
    private PautaService pautaService;
    @Mock
    private PautaRepository pautaRepository;

    @Test
    @DisplayName("Deve criar uma pauta com sucesso")
    void deveCriarPautaComSucesso() {
        String titulo = "Assembleia Geral";
        String descricao = "Discussão sobre orçamento";

        Pauta pautaSalva = Pauta.builder()
                .id(1L)
                .titulo(titulo)
                .descricao(descricao)
                .build();

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);

        Pauta resultado = pautaService.criarPauta(titulo, descricao);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(titulo, resultado.getTitulo());
        assertEquals(descricao, resultado.getDescricao());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    @DisplayName("Deve buscar pauta por ID com sucesso quando existir")
    void deveBuscarPautaPorIdComSucesso() {
        Long pautaId = 1L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta teste").build();

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        Pauta resultado = pautaService.buscarPorId(pautaId);

        assertNotNull(resultado);
        assertEquals(pautaId, resultado.getId());
        verify(pautaRepository, times(1)).findById(pautaId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar pauta por ID inexistente")
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        Long pautaId = 1L;
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> pautaService.buscarPorId(pautaId)
        );

        assertTrue(exception.getMessage().contains("Pauta não encontrada com ID: " + pautaId));
        verify(pautaRepository, times(1)).findById(pautaId);
    }
}

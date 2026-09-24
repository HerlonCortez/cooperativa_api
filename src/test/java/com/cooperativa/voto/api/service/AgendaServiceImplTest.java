package com.cooperativa.voto.api.service;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.exception.RecursoNaoEncontradoException;
import com.cooperativa.voto.api.domain.service.impl.AgendaServiceImpl;
import com.cooperativa.voto.api.infrastructure.repository.AgendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendaServiceImplTest {
    @InjectMocks
    private AgendaServiceImpl agendaServiceImpl;
    @Mock
    private AgendaRepository agendaRepository;

    @Test
    @DisplayName("Deve criar uma pauta com sucesso")
    void deveCriarPautaComSucesso() {
        String title = "Assembleia Geral";
        String description = "Discussão sobre orçamento";

        Pauta pautaSalva = Pauta.builder()
                .id(1L)
                .titulo(title)
                .descricao(description)
                .build();

        when(agendaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);

        Pauta resultado = agendaServiceImpl.createAgenda(title, description);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(title, resultado.getTitulo());
        assertEquals(description, resultado.getDescricao());
        verify(agendaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    @DisplayName("Deve buscar pauta por ID com sucesso quando existir")
    void deveBuscarPautaPorIdComSucesso() {
        Long pautaId = 1L;
        Pauta pauta = Pauta.builder().id(pautaId).titulo("Pauta teste").build();

        when(agendaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        Pauta resultado = agendaServiceImpl.getAgendaId(pautaId);

        assertNotNull(resultado);
        assertEquals(pautaId, resultado.getId());
        verify(agendaRepository, times(1)).findById(pautaId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar pauta por ID inexistente")
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        Long pautaId = 1L;
        when(agendaRepository.findById(pautaId)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> agendaServiceImpl.getAgendaId(pautaId)
        );

        assertTrue(exception.getMessage().contains("Pauta não encontrada com ID: " + pautaId));
        verify(agendaRepository, times(1)).findById(pautaId);
    }
}

package com.cooperativa.voto.api.service;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import com.cooperativa.voto.api.domain.exception.RegraNegocioException;
import com.cooperativa.voto.api.domain.service.impl.AgendaServiceImpl;
import com.cooperativa.voto.api.domain.service.impl.VotingSessionServiceImpl;
import com.cooperativa.voto.api.infrastructure.repository.VotingSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotingSessionServiceImplTest {
    @InjectMocks
    private VotingSessionServiceImpl votingSessionServiceImpl;
    @Mock
    private AgendaServiceImpl agendaServiceImpl;
    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Test
    @DisplayName("Deve abrir sessão com tempo padrão de 1 minuto quando tempoMinutos for nulo")
    void deveAbrirSessaoComTempoPadrao() {
        Long agendaId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);

        Pauta agenda = Pauta.builder()
                .id(agendaId)
                .titulo("Pauta")
                .build();
        SessaoVotacao session = SessaoVotacao.builder()
                .id(1L)
                .pauta(agenda)
                .dataAbertura(start)
                .dataFechamento(end)
                .build();

        when(agendaServiceImpl.getAgendaId(agendaId)).thenReturn(agenda);
        when(votingSessionRepository.existsByPautaId(agendaId)).thenReturn(false);
        when(votingSessionRepository.save(any(SessaoVotacao.class))).thenReturn(session);

        SessaoVotacao result = votingSessionServiceImpl.openSession(agendaId,null);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(agendaId, result.getPauta().getId());
        assertTrue(result.getDataFechamento().isAfter(result.getDataAbertura()));
        verify(votingSessionRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    @DisplayName("Deve abrir sessão com tempo determinado quando tempoMinutos for maior que zero")
    void deveAbrirSessaoComTempoDeterminadoEmMinutos() {
        Long agendaId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(3);

        Pauta agenda = Pauta.builder()
                .id(agendaId)
                .titulo("Pauta")
                .build();
        SessaoVotacao session = SessaoVotacao.builder()
                .id(1L)
                .pauta(agenda)
                .dataAbertura(start)
                .dataFechamento(end)
                .build();

        when(agendaServiceImpl.getAgendaId(agendaId)).thenReturn(agenda);
        when(votingSessionRepository.existsByPautaId(agendaId)).thenReturn(false);
        when(votingSessionRepository.save(any(SessaoVotacao.class))).thenReturn(session);

        SessaoVotacao result = votingSessionServiceImpl.openSession(agendaId,3);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(agendaId, result.getPauta().getId());
        assertTrue(result.getDataFechamento().isAfter(result.getDataAbertura()));
        verify(votingSessionRepository, times(1)).save(any(SessaoVotacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar abrir sessão para pauta que já possui sessão aberta")
    void deveLancarExcecaoQuandoSessaoJaExiste() {
        Long agendaId = 1L;
        Pauta agenda = Pauta.builder()
                .id(agendaId)
                .titulo("Pauta")
                .build();

        when(agendaServiceImpl.getAgendaId(agendaId)).thenReturn(agenda);
        when(votingSessionRepository.existsByPautaId(agendaId)).thenReturn(true);

       RegraNegocioException exception = assertThrows(RegraNegocioException.class,
               () -> votingSessionServiceImpl.openSession(agendaId,2));

       assertTrue(exception.getMessage().contains("Já existe uma sessão de votação criada para esta pauta."));
       verify(votingSessionRepository, never()).save(any(SessaoVotacao.class));
    }
}

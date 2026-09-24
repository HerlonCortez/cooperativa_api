package com.cooperativa.voto.api.service;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import com.cooperativa.voto.api.domain.entity.Voto;
import com.cooperativa.voto.api.domain.service.impl.VoteServiceImpl;
import com.cooperativa.voto.api.domain.service.impl.VotingSessionServiceImpl;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import com.cooperativa.voto.api.infrastructure.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceImplTest {
    @InjectMocks
    private VoteServiceImpl voteService;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VotingSessionServiceImpl votingSessionService;

    @Test
    @DisplayName("")
    void deveRegistrarUmVotComSucesso() {
        Long agendaId = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        String cpf = "12345678900";

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
        Voto vote = Voto.builder()
                .pauta(session.getPauta())
                .associadoId(cpf)
                .opcao(OptionVoteEnum.SIM)
                .build();

        when(voteRepository.save(any(Voto.class))).thenReturn(vote);
        when(votingSessionService.getAgendaId(agendaId)).thenReturn(session);
        when(voteRepository.existsByPautaIdAndAssociadoId(agendaId, cpf)).thenReturn(Boolean.FALSE);

        voteService.castVote(agendaId, cpf, OptionVoteEnum.SIM);

        verify(voteRepository, times(1)).save(any(Voto.class));
    }
}

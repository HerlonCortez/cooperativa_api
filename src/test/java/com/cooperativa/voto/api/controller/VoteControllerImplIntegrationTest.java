package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.RecordVoteRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import com.cooperativa.voto.api.domain.service.CpfValidationService;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import com.cooperativa.voto.api.infrastructure.repository.AgendaRepository;
import com.cooperativa.voto.api.infrastructure.repository.VoteRepository;
import com.cooperativa.voto.api.infrastructure.repository.VotingSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class VoteControllerImplIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VotingSessionRepository votingSessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @MockitoBean
    private CpfValidationService cpfValidationService;

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        votingSessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo completo: Persistir Pauta, Sessão Aberta, Registrar Voto e Apurar Resultado")
    void deveExecutarFluxoCompletoDeVotacaoEApuracao() throws Exception {
        doNothing().when(cpfValidationService).validateMemberIsEligibleToVote(anyString());

        var pauta = agendaRepository.save(
                Pauta.builder()
                        .titulo("Assembleia Geral Anual")
                        .descricao("Aprovação das contas do exercício")
                        .build()
        );

        votingSessionRepository.save(
                SessaoVotacao.builder()
                        .pauta(pauta)
                        .dataAbertura(LocalDateTime.now())
                        .dataFechamento(LocalDateTime.now().plusMinutes(10))
                        .build()
        );

        var votoSimRequest = new RecordVoteRequestDTO(pauta.getId(), "01234567890", OptionVoteEnum.SIM.SIM);

        mockMvc.perform(post("/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votoSimRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Voto registrado com sucesso!"));

        var votoSimRequest2 = new RecordVoteRequestDTO(pauta.getId(), "98765432100", OptionVoteEnum.SIM);

        mockMvc.perform(post("/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votoSimRequest2)))
                .andExpect(status().isCreated());

        assertEquals(2, voteRepository.countByPautaId(pauta.getId()));

        mockMvc.perform(get("/v1/votos/resultado/{pautaId}", pauta.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId").value(pauta.getId()))
                .andExpect(jsonPath("$.totalVotes").value(2))
                .andExpect(jsonPath("$.yesVotes").value(2))
                .andExpect(jsonPath("$.noVotes").value(0))
                .andExpect(jsonPath("$.result").value("APROVADA"));
    }
}

package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.CreateAgendaRequestDTO;
import com.cooperativa.voto.api.controller.dto.OpenSessionRequestDTO;
import com.cooperativa.voto.api.infrastructure.repository.AgendaRepository;
import com.cooperativa.voto.api.infrastructure.repository.VoteRepository;
import com.cooperativa.voto.api.infrastructure.repository.VotingSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class VotingSessionControllerIntegrationTest {
    @Autowired
    AgendaRepository agendaRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VotingSessionRepository votingSessionRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        votingSessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo completo: abrir sessão de votação e consultar estado da sessão")
    void deveAbrirEBuscarSessaoDeVotacao() throws Exception {
        var agendaRequest = new CreateAgendaRequestDTO("Pauta Sessão", "Descrição da Pauta");
        MvcResult pautaResult = mockMvc.perform(post("/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        Integer agendaId = JsonPath.parse(pautaResult.getResponse().getContentAsString()).read("$.id");

        var sessaoRequest = new OpenSessionRequestDTO(agendaId.longValue(), 10);

        mockMvc.perform(post("/v1/sessoes/abrir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessaoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.agendaId").value(agendaId))
                .andExpect(jsonPath("$.open").value(true));

        mockMvc.perform(get("/v1/sessoes/pauta/{pautaId}", agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId").value(agendaId))
                .andExpect(jsonPath("$.open").value(true));
    }
}

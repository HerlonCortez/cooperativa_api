package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.CreateAgendaRequestDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AgendaControllerImplIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AgendaRepository agendaRepository;
    @Autowired
    private VotingSessionRepository votingSessionRepository;
    @Autowired
    private VoteRepository voteRepository;

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        votingSessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    @DisplayName("Fluxo completo: Persistir pauta no banco e consultar via endpoint REST")
    void deveCriarEBuscarPautaNoBancoDeDados() throws Exception {
        var request = new CreateAgendaRequestDTO("Pauta de Integração", "Descrição da pauta");

        String responseJson = mockMvc.perform(post("/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Pauta de Integração"))
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, agendaRepository.count());

        Long pautaIdGerada = objectMapper.readTree(responseJson).get("id").asLong();

        mockMvc.perform(get("/v1/pautas/" + pautaIdGerada))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pautaIdGerada))
                .andExpect(jsonPath("$.titulo").value("Pauta de Integração"));
    }
}

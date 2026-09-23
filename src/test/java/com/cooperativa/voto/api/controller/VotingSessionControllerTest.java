package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.OpenSessionRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.entity.SessaoVotacao;
import com.cooperativa.voto.api.domain.service.VotingSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VotingSessionControllerTest {
    @InjectMocks
    private VotingSessionController votingSessionController;

    @Mock
    private VotingSessionService votingSessionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Pauta agenda;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(votingSessionController).build();
        objectMapper = new ObjectMapper();

        agenda = Pauta.
                builder()
                .id(1L)
                .titulo("Nova Pauta")
                .descricao("Descrição da pauta")
                .build();
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) e os dados da sessão ao abrir com sucesso")
    void deveAbrirSessaoComSucesso() throws Exception {
        Long agendaId = 1L;
        Integer timeMinutes = 5;
        OpenSessionRequestDTO request = new OpenSessionRequestDTO(agendaId, timeMinutes);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(timeMinutes);

        SessaoVotacao sessaoMock = SessaoVotacao.builder()
                .id(10L)
                .pauta(agenda)
                .dataAbertura(start)
                .dataFechamento(end)
                .build();

        when(votingSessionService.openSession(eq(agendaId), eq(timeMinutes))).thenReturn(sessaoMock);

        mockMvc.perform(post("/v1/sessoes/abrir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.agendaId").value(agendaId))
                .andExpect(jsonPath("$.open").value(true));

        verify(votingSessionService, times(1)).openSession(agendaId, timeMinutes);
    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) quando o pautaId do request for nulo")
    void deveRetornarBadRequestQuandoPautaIdNulo() throws Exception {
        OpenSessionRequestDTO requestInvalid = new OpenSessionRequestDTO(null, 10);

        mockMvc.perform(post("/v1/sessoes/abrir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalid)))
                .andExpect(status().isBadRequest());

        verify(votingSessionService, never()).openSession(any(), any());
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) ao buscar sessão existente por ID da pauta")
    void deveBuscarSessaoPorPautaId() throws Exception {
        // Arrange
        Long agendaId = 1L;
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusMinutes(10);

        SessaoVotacao sessionMock = SessaoVotacao.builder()
                .id(10L)
                .pauta(agenda)
                .dataAbertura(inicio)
                .dataFechamento(fim)
                .build();

        when(votingSessionService.getAgendaId(agendaId)).thenReturn(sessionMock);

        // Act & Assert
        mockMvc.perform(get("/v1/sessoes/pauta/{pautaId}", agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.agendaId").value(agendaId))
                .andExpect(jsonPath("$.open").value(true));

        verify(votingSessionService, times(1)).getAgendaId(agendaId);
    }
}

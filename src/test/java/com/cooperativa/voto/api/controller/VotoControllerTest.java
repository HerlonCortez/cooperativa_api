package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.RecordVoteRequestDTO;
import com.cooperativa.voto.api.domain.dto.ResultVotingDTO;
import com.cooperativa.voto.api.domain.service.impl.VoteServiceImpl;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import com.cooperativa.voto.api.infrastructure.repository.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VotoControllerTest {
    @InjectMocks
    private VoteControllerImpl voteControllerImpl;

    @Mock
    private VoteServiceImpl voteService;
    @Mock
    private VoteRepository voteRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
       mockMvc = MockMvcBuilders.standaloneSetup(voteControllerImpl).build();
    }

    @Test
    @DisplayName("Deve retornar Status 201 (Created) ao registrar voto válido")
    void deveRegistrarVotoComSucesso() throws Exception {
        var request = new RecordVoteRequestDTO(1L, "12345678900", OptionVoteEnum.SIM.SIM);
        doNothing().when(voteService).castVote(eq(1L), eq("12345678900"), eq(OptionVoteEnum.SIM.SIM));

        mockMvc.perform(post("/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Voto registrado com sucesso!"));

        verify(voteService, times(1)).castVote(1L, "12345678900", OptionVoteEnum.SIM);
    }

    @Test
    @DisplayName("Deve retornar Status 400 (Bad Request) quando o CPF não for informado")
    void deveRetornarBadRequestQuandoCpfEstiverEmBranco() throws Exception {
        var requestInvalid = new RecordVoteRequestDTO(1L, "", OptionVoteEnum.SIM);

        mockMvc.perform(post("/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalid)))
                .andExpect(status().isBadRequest());

        verify(voteService, never()).countVotes(any());
    }

    @Test
    @DisplayName("Deve retornar Status 200 (OK) e o resultado contabilizado da pauta")
    void deveRetornarResultadoDaVotacao() throws Exception {
        Long agendaId = 1L;
        var resultDTO = new ResultVotingDTO(agendaId, "Pauta de Teste", 10L, 7L, 3L, "APROVADA");

        when(voteService.countVotes(agendaId)).thenReturn(resultDTO);

        mockMvc.perform(get("/v1/votos/resultado/{pautaId}", agendaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agendaId").value(agendaId))
                .andExpect(jsonPath("$.agendaTitle").value("Pauta de Teste"))
                .andExpect(jsonPath("$.totalVotes").value(10))
                .andExpect(jsonPath("$.yesVotes").value(7))
                .andExpect(jsonPath("$.noVotes").value(3))
                .andExpect(jsonPath("$.result").value("APROVADA"));

        verify(voteService, times(1)).countVotes(agendaId);
    }
}

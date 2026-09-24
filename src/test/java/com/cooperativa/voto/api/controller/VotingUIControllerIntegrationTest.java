package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.NextSelectionRequestDTO;

import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.service.AgendaService;
import com.cooperativa.voto.api.domain.service.CpfValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotingUIController.class)
@TestPropertySource(properties = "app.sdui.base-url=http://localhost:8080")
class VotingUIControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AgendaService agendaService;

    @MockitoBean
    private CpfValidationService cpfValidationService;

    @Test
    @DisplayName("GET /v1/telas/voting/{agendaId} - Deve retornar status 200 e a estrutura da Tela 1")
    void getScreenFormIdentification_DeveRetornar200EJSONValido() throws Exception {
        // Arrange
        Long agendaId = 1L;
        Pauta agendaMock = new Pauta();
        agendaMock.setId(agendaId);
        agendaMock.setTitulo("Aprovação de Orçamento");

        when(agendaService.getAgendaId(agendaId)).thenReturn(agendaMock);

        mockMvc.perform(get("/v1/telas/voting/{agendaId}", agendaId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Identificação do Associado"))
                .andExpect(jsonPath("$.itens", hasSize(2)))
                .andExpect(jsonPath("$.nextButton.url").value("http://localhost:8080/v1/telas/selection-vote"))
                .andExpect(jsonPath("$.nextButton.body.agendaId").value(1));
    }

    @Test
    @DisplayName("POST /v1/telas/selection-vote - Deve retornar status 200 e as opções de voto")
    void getScreenSelecion_DeveRetornar200EJSONComOpcoesDeVoto() throws Exception {
        Long agendaId = 1L;
        String cpf = "12345678900";
        NextSelectionRequestDTO request = new NextSelectionRequestDTO(agendaId, cpf);

        Pauta agendaMock = new Pauta();
        agendaMock.setId(agendaId);
        agendaMock.setTitulo("Aprovação de Orçamento");

        when(agendaService.getAgendaId(agendaId)).thenReturn(agendaMock);

        mockMvc.perform(post("/v1/telas/selection-vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Escolha se votoAprovação de Orçamento"))
                .andExpect(jsonPath("$.itens", hasSize(3)))
                .andExpect(jsonPath("$.itens[0].text").value("Votar SIM"))
                .andExpect(jsonPath("$.itens[0].body.vote").value("SIM"))
                .andExpect(jsonPath("$.itens[1].text").value("Votar Não"))
                .andExpect(jsonPath("$.itens[1].body.vote").value("NAO"));
    }

    @Test
    @DisplayName("POST /v1/telas/selection-vote - Deve retornar 400 Bad Request quando o payload for inválido")
    void getScreenSelecion_DeveRetornar400QuandoRequestInvalida() throws Exception {
        NextSelectionRequestDTO invalidRequest = new NextSelectionRequestDTO(null, null);

        mockMvc.perform(post("/v1/telas/selection-vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}

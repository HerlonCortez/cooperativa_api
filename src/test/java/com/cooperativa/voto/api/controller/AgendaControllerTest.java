package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.CreateAgendaRequestDTO;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.service.AgendaService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AgendaControllerTest {
    @InjectMocks
    private AgendaController agendaController;

    @Mock
    private AgendaService agendaService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(agendaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Deve criar pauta com sucesso")
    public void deveCriarPauta() throws Exception {
        var request = new CreateAgendaRequestDTO("Nova Pauta", "Descrição da pauta");
        var pautaSlava = Pauta.
                builder()
                .id(1L)
                .titulo("Nova Pauta")
                .descricao("Descrição da pauta")
                .build();

        when(agendaService.createAgenda("Nova Pauta", "Descrição da pauta")).thenReturn(pautaSlava);

        mockMvc.perform(post("/v1/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Nova Pauta"));
    }
}

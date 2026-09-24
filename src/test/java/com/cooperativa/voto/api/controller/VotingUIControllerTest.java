package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.NextSelectionRequestDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ScreenFormResponseDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ScreenSelectionResponse;
import com.cooperativa.voto.api.domain.entity.Pauta;
import com.cooperativa.voto.api.domain.service.AgendaService;
import com.cooperativa.voto.api.domain.service.CpfValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotingUIControllerTest {

    @Mock
    private AgendaService agendaService;

    @Mock
    private CpfValidationService cpfValidationService;

    @InjectMocks
    private VotingUIController votingUIController;

    private final String BASE_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(votingUIController, "baseUrl", BASE_URL);
    }

    @Test
    @DisplayName("Deve retornar a tela de formulário SDUI com sucesso")
    void getScreenFormIdentification_Sucesso() {
        Long agendaId = 1L;
        Pauta agendaMock = new Pauta();
        agendaMock.setId(agendaId);
        agendaMock.setTitulo("Aprovação de Contas");

        when(agendaService.getAgendaId(agendaId)).thenReturn(agendaMock);

        ResponseEntity<ScreenFormResponseDTO> response = votingUIController.getScreenFormIdentification(agendaId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(agendaService, times(1)).getAgendaId(agendaId);
    }

    @Test
    @DisplayName("Deve retornar a tela de seleção de voto SDUI com sucesso")
    void getScreenSelecion_Sucesso() {
         Long agendaId = 1L;
        String cpf = "12345678900";
        NextSelectionRequestDTO request = new NextSelectionRequestDTO(agendaId, cpf);

        Pauta agendaMock = new Pauta();
        agendaMock.setId(agendaId);
        agendaMock.setTitulo("Aprovação de Contas");

        when(agendaService.getAgendaId(agendaId)).thenReturn(agendaMock);

        ResponseEntity<ScreenSelectionResponse> response = votingUIController.getScreenSelecion(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(agendaService, times(1)).getAgendaId(agendaId);
    }
}

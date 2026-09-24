package com.cooperativa.voto.api.controller;

import com.cooperativa.voto.api.controller.dto.NextSelectionRequestDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ComponentDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ItemFormDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ScreenFormResponseDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ScreenSelectionResponse;
import com.cooperativa.voto.api.domain.service.AgendaService;
import com.cooperativa.voto.api.domain.service.CpfValidationService;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/telas")
public class VotingUIController {
    private final AgendaService agendaService;
    private final CpfValidationService cpfValidationService;

    @Value("${app.sdui.base-url}")
    private String baseUrl;

    @GetMapping("/voting/{agendaId}")
    public ResponseEntity<ScreenFormResponseDTO> getScreenFormIdentification(@PathVariable("agendaId") Long agendaId) {
        var agenda = agendaService.getAgendaId(agendaId);

        var itens = List.of(
                ItemFormDTO.textInformation("Pauta" + agenda.getTitulo()),
                ItemFormDTO.inputText("textFieldId", "Informe o seu CPF para continuar")
        );

        var nextButton = new ComponentDTO(
                "Avançar para votação",
                baseUrl + "/v1/telas/selection-vote",
                Map.of("agendaId", agendaId)
        );

        var cancelButton = new ComponentDTO(
                "Cancelar",
                baseUrl + "v1/pautas",
                Map.of()
        );

        var screenForm = ScreenFormResponseDTO.create(
                "Identificação do Associado",
                itens,
                nextButton,
                cancelButton
        );

        return ResponseEntity.ok(screenForm);
    }

    @PostMapping("/selection-vote")
    public ResponseEntity<ScreenSelectionResponse> getScreenSelecion(@RequestBody @Valid NextSelectionRequestDTO request) {
//        cpfValidationService.validateMemberIsEligibleToVote(request.getCpf());
        var agenda = agendaService.getAgendaId(request.agendaId());
        String urlRecordVote = baseUrl + "/v1/votos";

        var optionYes = new ComponentDTO(
                "Votar SIM",
                urlRecordVote,
                Map.of("agendaId", request.agendaId(), "cpf", request.getCpf(), "vote", OptionVoteEnum.SIM)
        );

        var optionNo = new ComponentDTO(
                "Votar Não",
                urlRecordVote,
                Map.of("agendaId", request.agendaId(), "cpf", request.getCpf(), "vote", OptionVoteEnum.NAO)
        );

        var optionBack = new ComponentDTO(
                "Voltar",
                baseUrl + "/v1/telas/voting/" + request.agendaId(),
                Map.of()
        );

        var screenSelection = ScreenSelectionResponse.create(
                "Escolha se voto" + agenda.getTitulo(),
                List.of(optionYes, optionNo, optionBack)
        );

        return ResponseEntity.ok(screenSelection);
    }
}

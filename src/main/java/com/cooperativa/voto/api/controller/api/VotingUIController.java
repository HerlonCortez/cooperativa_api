package com.cooperativa.voto.api.controller.api;

import com.cooperativa.voto.api.controller.dto.NextSelectionRequestDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ComponentDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ItemFormDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ScreenFormResponseDTO;
import com.cooperativa.voto.api.controller.dto.sdui.ScreenSelectionResponse;
import com.cooperativa.voto.api.domain.service.AgendaService;
import com.cooperativa.voto.api.domain.service.CpfValidationService;
import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Tela UI", description = "Operações para rederização da tela UI")
@RequestMapping("/v1/telas")
public interface VotingUIController {
    @GetMapping("/voting/{agendaId}")
    @Operation(summary = "Abertura da tela Formulário.")
    ResponseEntity<ScreenFormResponseDTO> getScreenFormIdentification(@PathVariable("agendaId") Long agendaId);

    @PostMapping("/selection-vote")
    @Operation(summary = "Abertura da tela Seleção.")
    ResponseEntity<ScreenSelectionResponse> getScreenSelecion(@RequestBody @Valid NextSelectionRequestDTO request);
}

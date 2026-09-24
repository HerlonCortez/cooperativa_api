package com.cooperativa.voto.api.controller.dto;

import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NextSelectionRequestDTO(
        @NotNull(message = "O ID da pauta é obrigatório")
        Long agendaId,

        @JsonProperty("textFieldId")
        @NotBlank(message = "O CPF do associado é obrigatório")
        String textFieldId

) {
    public String getCpf() {
        return textFieldId;
    }
}

package com.cooperativa.voto.api.controller.dto;

import com.cooperativa.voto.api.infrastructure.enums.OptionVoteEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecordVoteRequestDTO(@NotNull(message = "O ID da pauta é obrigatório")
                                   Long agendaId,

                                   @NotBlank(message = "O CPF do associado é obrigatório")
                                   String textFieldId, // Corresponde ao ID do input configurado no JSON SDUI

                                   @NotNull(message = "A opção de voto é obrigatória")
                                   OptionVoteEnum vote
) {
    public String getCpf() {
        return textFieldId;
    }
}

package com.cooperativa.voto.api.controller.dto.sdui;

import java.util.List;

public record ScreenFormResponseDTO(
        String type,
        String title,
        List<ItemFormDTO> itens,
        ComponentDTO nextButton,
        ComponentDTO cancelButton
) {
    public static ScreenFormResponseDTO create(String title, List<ItemFormDTO> itens, ComponentDTO buttonOK, ComponentDTO cancelButton) {
       return new ScreenFormResponseDTO(
               "FORMULARIO",
               title,
               itens,
               buttonOK,
               cancelButton
       );
    }
}

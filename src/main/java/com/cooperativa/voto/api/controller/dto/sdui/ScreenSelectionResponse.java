package com.cooperativa.voto.api.controller.dto.sdui;

import java.util.List;

public record ScreenSelectionResponse(
        String type,
        String title,
        List<ComponentDTO> itens
) {
    public static ScreenSelectionResponse create(String title, List<ComponentDTO> itens) {
       return  new ScreenSelectionResponse("SELECAO", title, itens);
    }
}

package com.cooperativa.voto.api.controller.dto.sdui;

public record ItemFormDTO(
        String type,
        String id,
        String title,
        Object value,
        String text
) {
    public static ItemFormDTO textInformation(String text) {
        return new ItemFormDTO("TEXTO", null, null, null, text);
    }

    public static ItemFormDTO inputText(String id, String title) {
        return new ItemFormDTO("INPUT_TEXTO", id, title, null, null);
    }
}

package com.cooperativa.voto.api.controller.dto.sdui;

import java.util.Map;

public record ComponentDTO(
        String text,
        String url,
        Map<String, Object> body
) {
}

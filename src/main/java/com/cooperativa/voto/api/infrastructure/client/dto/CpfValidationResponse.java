package com.cooperativa.voto.api.infrastructure.client.dto;

public record CpfValidationResponse(
        String status
) {
    public boolean canVote() {
        return "ABLE_TO_VOTE".equalsIgnoreCase(status);
    }
}

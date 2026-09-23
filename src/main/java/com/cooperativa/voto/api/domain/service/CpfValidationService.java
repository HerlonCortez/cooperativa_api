package com.cooperativa.voto.api.domain.service;

public interface CpfValidationService {
    void validateMemberIsEligibleToVote(String cpf);
}

package com.cooperativa.voto.api.domain.service.impl;

import com.cooperativa.voto.api.domain.service.CpfValidationService;
import com.cooperativa.voto.api.domain.service.VoteService;
import com.cooperativa.voto.api.infrastructure.client.UserInfoClient;
import com.cooperativa.voto.api.infrastructure.client.dto.CpfValidationResponse;
import com.cooperativa.voto.api.infrastructure.client.exception.AssociadoNaoAutorizadoException;
import com.cooperativa.voto.api.infrastructure.client.exception.CpfInvalidoException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class CpfValidationServiceImpl implements CpfValidationService {
    private final UserInfoClient userInfoClient;

    @Override
    public void validateMemberIsEligibleToVote(String cpf) {
        try {
            log.info("Validando member iseligible to vote");
            CpfValidationResponse validationResponse = userInfoClient.validarCpf(cpf);

            log.info("Validando member iseligible to vote");
            if (validationResponse == null ||validationResponse.canVote()) {
                throw new AssociadoNaoAutorizadoException("Associado de CPF " + cpf + " não está autorizado a votar.");
            }
        } catch (FeignException.NotFound e) {
            throw new CpfInvalidoException("CPF informado é inválido: " + cpf);
        } catch (FeignException e) {
            throw new RuntimeException("Erro ao comunicar com o serviço externo de validação de CPF.", e);
        }
    }
}

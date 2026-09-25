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
            log.info("Iniciando a validação do CPF para um associado");
            CpfValidationResponse validationResponse = userInfoClient.validarCpf(cpf);

            log.info("Validando se o associado pode votar");
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

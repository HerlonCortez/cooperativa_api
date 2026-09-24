package com.cooperativa.voto.api.infrastructure.client;

import com.cooperativa.voto.api.infrastructure.client.dto.CpfValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "userInfoClient",
        url = "${app.user-info-url}"
)
public interface UserInfoClient {

    @GetMapping("/users/{cpf}")
    CpfValidationResponse validarCpf(@PathVariable("cpf") String cpf);
}

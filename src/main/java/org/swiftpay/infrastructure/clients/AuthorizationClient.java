package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.swiftpay.dtos.AuthorizationDTO;

@FeignClient(url = "https://util.devi.tools/api/v2/authorize", name = "authorization")
public interface AuthorizationClient {

    @GetMapping
    AuthorizationDTO validateTransference ();

}

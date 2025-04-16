package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.WalletResponseDTO;

@FeignClient(url = "${asaas.url}/wallets", name= "AsaasWalletClient", configuration = AsaasConfig.class)
public interface AsaasWalletClient {

    @GetMapping("/")
    WalletResponseDTO createWalletIdForUser();

}

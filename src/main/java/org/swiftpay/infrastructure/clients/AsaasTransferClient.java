package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.TransferRequestDTO;

@FeignClient(url = "${asaas.url}/transfers", name= "AsaasTransferClient", configuration = AsaasConfig.class)
public interface AsaasTransferClient {

    @PostMapping("/")
    void transferToAsaasAccount (@RequestBody TransferRequestDTO transferRequestDTO);

}

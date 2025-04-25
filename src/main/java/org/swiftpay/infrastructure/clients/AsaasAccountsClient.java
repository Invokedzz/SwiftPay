package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.CustomerResponseDTO;
import org.swiftpay.dtos.RegisterDTO;

@FeignClient(url = "${asaas.url}/accounts", name= "AsaasCustomerClient", configuration = AsaasConfig.class)
public interface AsaasAccountsClient {

    @PostMapping
    CustomerResponseDTO createCustomer (@RequestBody RegisterDTO registerDTO);

    @GetMapping("/{id}")
    CustomerResponseDTO getCustomer (@PathVariable String id);

}

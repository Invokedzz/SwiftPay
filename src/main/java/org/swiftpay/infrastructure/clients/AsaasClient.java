package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.PaymentRequestDTO;
import org.swiftpay.dtos.RegisterDTO;
import org.swiftpay.dtos.TransferResponseDTO;

@FeignClient(url = "${asaas.url}", name= "AsaasClient", configuration = AsaasConfig.class)
public interface AsaasClient {

    @PostMapping("/customers")
    void createCustomer (@RequestBody RegisterDTO registerDTO);

    @PostMapping("/payments")
    TransferResponseDTO createPayment (@RequestBody PaymentRequestDTO paymentRequestDTO);

}

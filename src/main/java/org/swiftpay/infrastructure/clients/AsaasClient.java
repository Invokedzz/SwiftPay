package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.*;

@FeignClient(url = "${asaas.url}", name= "AsaasClient", configuration = AsaasConfig.class)
public interface AsaasClient {

    @PostMapping("/customers")
    CustomerResponseDTO createCustomer (@RequestBody CustomerRequestDTO customerRequestDTO);

    @PostMapping("/payments")
    TransferResponseDTO createPayment (@RequestBody PaymentRequestDTO paymentRequestDTO);

}

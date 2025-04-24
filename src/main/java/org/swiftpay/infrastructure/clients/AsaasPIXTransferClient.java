package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.swiftpay.configuration.AsaasConfig;

@FeignClient(url = "https://api-sandbox.asaas.com/v3/pix/qrCodes", name = "AsaasPIXTransferClient", configuration = AsaasConfig.class)
public interface AsaasPIXTransferClient {

    @GetMapping("/transactions/{id}")
    void getIndividualTransaction (@PathVariable String id);

    @GetMapping("/transactions")
    void getTransactions ();

}

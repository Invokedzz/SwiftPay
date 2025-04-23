package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.swiftpay.configuration.AsaasConfig;

@FeignClient(url = "https://api-sandbox.asaas.com/v3/pix", name = "AsaasPIXClient", configuration = AsaasConfig.class)
public interface AsaasPIXClient {

    @PostMapping("/addressKeys")
    void generatePIXKey ();

    @PostMapping("/qrCodes/static")
    void generateQRCode ();

    @GetMapping("/addressKeys")
    void getKeys ();

    @GetMapping("/addressKeys/{id}")
    void getIndividualKey (@PathVariable String id);

    @DeleteMapping("/addressKeys/{id}")
    void deletePIXKey (@PathVariable String id);

    @DeleteMapping("/qrCodes/static/{id}")
    void deleteQRCode (@PathVariable String id);

}

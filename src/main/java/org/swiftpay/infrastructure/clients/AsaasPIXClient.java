package org.swiftpay.infrastructure.clients;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.dtos.PIXKeyData;
import org.swiftpay.dtos.PIXKeyRequestDTO;
import org.swiftpay.dtos.PIXKeyResponseDTO;

@Headers("Content-Type: application/json")
@FeignClient(url = "https://api-sandbox.asaas.com/v3/pix", name = "AsaasPIXClient", configuration = AsaasConfig.class)
public interface AsaasPIXClient {

    @PostMapping("/addressKeys")
    PIXKeyResponseDTO generatePIXKey (@RequestBody PIXKeyRequestDTO PIXKeyRequestDTO);

    @GetMapping("/addressKeys")
    PIXKeyData getKeys ();

    @GetMapping("/addressKeys/{id}")
    PIXKeyDTO getIndividualKey (@PathVariable String id);

    @DeleteMapping("/addressKeys/{id}")
    void deletePIXKey (@PathVariable String id);

}

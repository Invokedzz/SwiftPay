package org.swiftpay.infrastructure.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.configuration.AsaasConfig;
import org.swiftpay.dtos.TransferRequestDTO;
import org.swiftpay.dtos.TransferResponseDTO;
import org.swiftpay.dtos.TransferStatusDTO;

import java.time.LocalDate;
import java.util.List;

@FeignClient(url = "${asaas.url}/transfers", name= "AsaasTransferClient", configuration = AsaasConfig.class)
public interface AsaasTransferClient {

    @PostMapping
    TransferResponseDTO transferToBankAccounts (@RequestBody TransferRequestDTO transferRequestDTO);

    @PostMapping("/")
    TransferResponseDTO transferToAsaasAccount (@RequestBody TransferRequestDTO transferRequestDTO);

    @GetMapping
    List <TransferStatusDTO> getTransfers (@RequestParam LocalDate createdAt);

    @GetMapping("/{id}")
    TransferStatusDTO getIndividualTransfer (@PathVariable String id);

    @DeleteMapping("/{id}/cancel")
    TransferStatusDTO cancelTransfer (@PathVariable String id);

}

package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.services.TransferService;

@RestController
@RequestMapping("/transfer")
public record TransferController (TransferService transferService) {

    @PostMapping
    private ResponseEntity <Void> transfer (@Valid @RequestBody TransferDTO transferDTO) {

        return ResponseEntity.ok().body(null);

    }

    @PostMapping("/sandbox")
    private ResponseEntity <Void> transferSandboxVer (@Valid @RequestBody TransferDTO transferDTO) {

        transferService.transferToSomeoneSandbox(transferDTO);

        return ResponseEntity.accepted().build();

    }

}

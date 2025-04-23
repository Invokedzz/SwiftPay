package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.*;
import org.swiftpay.services.TransferService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public record TransferController (TransferService transferService) {

    @PostMapping("/")
    public ResponseEntity <TransferResponseDTO> transferToAsaasAccount (@RequestHeader HttpHeaders headers, @Valid @RequestBody TransferRequestDTO transferRequestDTO) {

        var response = transferService.transferToAsaasAccount(headers, transferRequestDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity <Void> confirmTransfer (@PathVariable String id) {

        transferService.confirmTransfer(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping
    public ResponseEntity <List<TransferStatusDTO>> getTransfers (@RequestHeader HttpHeaders headers, @RequestHeader LocalDate createdAt) {

        var transfers = transferService.getTransfers(headers, createdAt);

        return ResponseEntity.status(HttpStatus.OK).body(transfers);

    }

    @GetMapping("/{id}")
    public ResponseEntity <TransferStatusDTO> getIndividualTransfer (@PathVariable String id) {

        var transfer = transferService.getIndividualTransfer(id);

        return ResponseEntity.status(HttpStatus.OK).body(transfer);

    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity <TransferStatusDTO> cancelTransfer (@PathVariable String id) {

        var canceledTransfer = transferService.cancelTransfer(id);

        return ResponseEntity.status(HttpStatus.OK).body(canceledTransfer);

    }

}

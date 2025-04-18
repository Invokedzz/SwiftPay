package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.PaymentRequestDTO;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.services.TransferService;

@RestController
@RequestMapping("/transfer")
@Tag(name = "Transfer", description = "Endpoints related to transfer. Sandbox ver.")
public record TransferController (TransferService transferService) {

    @Hidden
    @PostMapping
    private ResponseEntity <Void> transfer (@Valid @RequestBody TransferDTO transferDTO) {

        transferService.transferToSomeone(transferDTO);

        return ResponseEntity.ok().body(null);

    }

    @PostMapping("/sandbox")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Have fun using the sandbox. Make transfers, and enjoy", responses = {

            @ApiResponse(

                    description = "Accepted",

                    responseCode = "201"

            ),

            @ApiResponse(

                    description = "Bad Request",

                    responseCode = "400"

            )

    })

    private ResponseEntity <Void> transferSandboxVer (@RequestHeader HttpHeaders headers, @Valid @RequestBody TransferDTO transferDTO) {

        transferService.transferToSomeoneSandbox(headers, transferDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

}

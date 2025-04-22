package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.TransferDTO;
import org.swiftpay.services.SandboxTransferService;

@RestController
@RequestMapping("/transfer-sandbox")
@Tag(name = "Transfer", description = "Endpoints related to transfer. Sandbox ver.")
public record SandboxTransferController(SandboxTransferService sandboxTransferService) {

    @PostMapping
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

        sandboxTransferService.transferToSomeoneSandbox(headers, transferDTO);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();

    }

}

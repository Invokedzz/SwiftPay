package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    private ResponseEntity <Void> transferSandboxVer (@Valid @RequestBody TransferDTO transferDTO) {

        transferService.transferToSomeoneSandbox(transferDTO);

        return ResponseEntity.accepted().build();

    }

}

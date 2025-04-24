package org.swiftpay.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.dtos.PIXKeyResponseDTO;
import org.swiftpay.services.PIXService;

import java.util.List;

@RestController
@RequestMapping("/keys")
public record PixController (PIXService pixService) {

    @PostMapping
    public ResponseEntity <PIXKeyResponseDTO> generatePIXKey () {

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/{id}")
    public ResponseEntity <PIXKeyDTO> getIndividualKey (@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping
    public ResponseEntity <List<PIXKeyDTO>> getKeys () {

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity <Void> deletePIXKey (@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}

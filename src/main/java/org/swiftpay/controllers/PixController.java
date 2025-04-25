package org.swiftpay.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.PIXKeyDTO;
import org.swiftpay.dtos.PIXKeyRequestDTO;
import org.swiftpay.dtos.PIXKeyResponseDTO;
import org.swiftpay.services.PIXService;

import java.util.List;

@RestController
@RequestMapping("/keys")
public record PixController (PIXService pixService) {

    @PostMapping
    public ResponseEntity <PIXKeyResponseDTO> generatePIXKey (@RequestHeader HttpHeaders headers, @RequestBody PIXKeyRequestDTO keyRequestDTO) {

        var response = pixService.generatePIXKey(headers, keyRequestDTO);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity <PIXKeyDTO> getIndividualKey (@RequestHeader HttpHeaders headers, @PathVariable String id) {

        var key = pixService.getIndividualKey(headers, id);

        return ResponseEntity.status(HttpStatus.OK).body(key);

    }

    @GetMapping
    public ResponseEntity <List<PIXKeyDTO>> getKeys () {

        var keys = pixService.getKeys();

        return ResponseEntity.status(HttpStatus.OK).body(keys);

    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity <Void> deletePIXKey (@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

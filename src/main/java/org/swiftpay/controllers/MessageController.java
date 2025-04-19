package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.AIResponseDTO;
import org.swiftpay.dtos.CreateMessageDTO;
import org.swiftpay.dtos.EditMessageDTO;
import org.swiftpay.services.MessageService;

@RestController
@RequestMapping("/api/message")
@Tag(name = "Message", description = "Endpoints related to chat messages. Don't try anything funny!")
public record MessageController (MessageService messageService) {

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    private ResponseEntity <AIResponseDTO> createMessage (@Valid @RequestBody CreateMessageDTO createMessageDTO) {

        var aiResponse = messageService.createMessage(createMessageDTO);

        return ResponseEntity.accepted().body(aiResponse);

    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    private ResponseEntity <Void> findMessageByText () {

        return ResponseEntity.ok().body(null);

    }

    @Hidden
    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editMessage (@Valid @RequestBody EditMessageDTO editMessageDTO, @PathVariable Long id) {

        messageService.editMessage(editMessageDTO, id);

        return ResponseEntity.ok().body(null);

    }

    @Hidden
    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteMessage (@PathVariable Long id) {

        messageService.deleteMessage(id);

        return ResponseEntity.ok().body(null);

    }

}

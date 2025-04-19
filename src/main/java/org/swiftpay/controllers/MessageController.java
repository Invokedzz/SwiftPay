package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.services.MessageService;

@RestController
@RequestMapping("/message")
@Tag(name = "Message", description = "Endpoints related to chat messages. Don't try anything funny!")
public record MessageController (MessageService messageService) {

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    private ResponseEntity <Void> createMessage () {

        return ResponseEntity.ok().body(null);

    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    private ResponseEntity <Void> findMessageByText () {

        return ResponseEntity.ok().body(null);

    }

    @Hidden
    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editMessage (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

    @Hidden
    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteMessage (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

}

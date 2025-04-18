package org.swiftpay.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.services.MessageService;

@RestController
@RequestMapping("/message")
public record MessageController (MessageService messageService) {

    @PostMapping
    private ResponseEntity <Void> createMessage () {

        return ResponseEntity.ok().body(null);

    }

    @GetMapping
    private ResponseEntity <Void> findMessageByText () {

        return ResponseEntity.ok().body(null);

    }

    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editMessage (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteMessage (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

}

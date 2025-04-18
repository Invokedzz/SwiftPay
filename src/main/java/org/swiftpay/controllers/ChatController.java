package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.CreateChatDTO;
import org.swiftpay.services.AIAssistantService;
import org.swiftpay.services.ChatService;

@RestController
public record ChatController (ChatService chatService) {

    @PostMapping
    private ResponseEntity <Void> createChat (@Valid @RequestBody CreateChatDTO createChatDTO) {

        return ResponseEntity.ok().body(null);

    }

    @GetMapping
    private ResponseEntity <Void> findChatById (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

    @GetMapping
    private ResponseEntity <Void> allChats () {

        return ResponseEntity.ok().body(null);

    }

    @DeleteMapping
    private ResponseEntity <Void> deleteChat (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

}

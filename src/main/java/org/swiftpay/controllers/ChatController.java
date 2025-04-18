package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.ChatDTO;
import org.swiftpay.dtos.CreateChatDTO;
import org.swiftpay.dtos.UserChatsDTO;
import org.swiftpay.services.AIAssistantService;
import org.swiftpay.services.ChatService;

import java.util.Set;

@RestController
@RequestMapping("/chat")
public record ChatController (ChatService chatService) {

    @PostMapping
    private ResponseEntity <Void> createChat (@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateChatDTO createChatDTO) {

        chatService.createChat(headers, createChatDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/{id}")
    private ResponseEntity <Set<UserChatsDTO>> findChatByUserId (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        var chats = chatService.findChatByUserId(headers, id);

        return ResponseEntity.ok().body(chats);

    }

    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editChat (@PathVariable Long id) {

        return ResponseEntity.ok().body(null);

    }

    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteChat (@PathVariable Long id) {

        chatService.deleteChat(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

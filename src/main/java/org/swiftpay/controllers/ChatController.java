package org.swiftpay.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.CreateChatDTO;
import org.swiftpay.dtos.EditChatDTO;
import org.swiftpay.dtos.UserChatsDTO;
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

    @GetMapping
    private ResponseEntity <Set<UserChatsDTO>> findChatByUserId (@RequestHeader HttpHeaders headers) {

        var chats = chatService.findChatByUserId(headers);

        return ResponseEntity.status(HttpStatus.OK).body(chats);

    }

    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editChat (@RequestHeader HttpHeaders headers, @Valid @RequestBody EditChatDTO editChatDTO, @PathVariable Long id) {

        chatService.editChat(headers, editChatDTO, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteChat (@PathVariable Long id) {

        chatService.deleteChat(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

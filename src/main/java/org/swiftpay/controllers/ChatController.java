package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Chat", description = "Endpoints related to chat. Create one, and see yourself.")
public record ChatController (ChatService chatService) {

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "This endpoint is used to create chats, so you can send your messages to the chatbot", responses = {

            @ApiResponse(

                    description = "Created",

                    responseCode = "201"

            ),

            @ApiResponse(

                    description = "Bad Request",

                    responseCode = "400"

            )

    })

    private ResponseEntity <Void> createChat (@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateChatDTO createChatDTO) {

        chatService.createChat(headers, createChatDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(description = "Endpoint created to store chats related to user", responses = {

            @ApiResponse(

                    description = "Ok",

                    responseCode = "200"

            ),

            @ApiResponse(

                    description = "Bad Request",

                    responseCode = "400"

            )

    })

    private ResponseEntity <Set<UserChatsDTO>> findChatByUserId (@RequestHeader HttpHeaders headers) {

        var chats = chatService.findChatByUserId(headers);

        return ResponseEntity.status(HttpStatus.OK).body(chats);

    }

    @Hidden
    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editChat (@RequestHeader HttpHeaders headers, @Valid @RequestBody EditChatDTO editChatDTO, @PathVariable Long id) {

        chatService.editChat(headers, editChatDTO, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Hidden
    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteChat (@PathVariable Long id) {

        chatService.deleteChat(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

package org.swiftpay.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.swiftpay.dtos.AIResponseDTO;
import org.swiftpay.dtos.CreateMessageDTO;
import org.swiftpay.dtos.EditMessageDTO;
import org.swiftpay.dtos.MessageDTO;
import org.swiftpay.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/message")
@Tag(name = "Message", description = "Endpoints related to chat messages.")
public record MessageController (MessageService messageService) {

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(responses = {

            @ApiResponse(

                    description = "Accepted",

                    responseCode = "202"

            )

    })

    private ResponseEntity <AIResponseDTO> createMessage (@Valid @RequestBody CreateMessageDTO createMessageDTO) {

        var aiResponse = messageService.createMessage(createMessageDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(aiResponse);

    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(responses = {

            @ApiResponse(

                    description = "Ok",

                    responseCode = "200"

            ),

            @ApiResponse(

                    description = "Forbidden",

                    responseCode = "403"

            ),

            @ApiResponse(

                    description = "Not Found",

                    responseCode = "404"

            )

    })
    private ResponseEntity <List<MessageDTO>> findMessagesByChatId (@RequestHeader HttpHeaders headers, @PathVariable Long id) {

        var messages = messageService.findChatMessages(headers, id);

        return ResponseEntity.status(HttpStatus.OK).body(messages);

    }

    @Hidden
    @PutMapping("/{id}/edit")
    private ResponseEntity <Void> editMessage (@Valid @RequestBody EditMessageDTO editMessageDTO, @PathVariable Long id) {

        messageService.editMessage(editMessageDTO, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @Hidden
    @DeleteMapping("/{id}/delete")
    private ResponseEntity <Void> deleteMessage (@PathVariable Long id) {

        messageService.deleteMessage(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}

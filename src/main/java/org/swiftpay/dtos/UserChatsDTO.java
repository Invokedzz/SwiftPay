package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "User Chats Data Access Object")
public record UserChatsDTO (

        @Schema(example = "How can I transfer to other users?")
        String title,

        @Schema(example = "2025-04-19")
        LocalDate createdAt

    ) {

    public UserChatsDTO (UserChatsDTO userChatsDTO) {

        this (userChatsDTO.title, userChatsDTO.createdAt);

    }
}

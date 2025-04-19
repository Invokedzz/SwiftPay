package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(name = "Create Chat Data Transfer Object", description = "Create a chat to talk with our assistant")
public record CreateChatDTO (

        @Schema(minLength = 4, maxLength = 100, example = "Create a beautiful title for your chats")
        @NotBlank(message = "Title field cannot be blank!")
        @Length(min = 4, max = 100, message = "Title field length must be bigger than 4 and lower than 100")
        String title,

        @NotNull
        @Schema(example = "38")
        Long userId

) {}

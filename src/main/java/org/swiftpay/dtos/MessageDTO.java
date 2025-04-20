package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Message Data Transfer Object")
public record MessageDTO (

        @Schema(example = "Hey. How are you?")
        String message,

        @Schema(example = "I'm fine. What about you?")
        String response

) {}

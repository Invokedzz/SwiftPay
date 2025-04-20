package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Create Message Data Transfer Object")
public record CreateMessageDTO (

        @Schema(example = "Explain more about SwiftPay, please")
        String message,

        @Schema(example = "11")
        Long chatId

) {}

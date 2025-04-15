package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "User Token Data Access Object")
public record UserTokenDTO (

        @Schema(description = "The user is going to obtain this token after logging in")
        String token

) {}

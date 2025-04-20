package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

@Schema(name = "AI Response Data Transfer Object")
public record AIResponseDTO (

        @Length(max = 500)
        String response

) {}

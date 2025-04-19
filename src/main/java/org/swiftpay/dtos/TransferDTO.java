package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(name = "Transfer Data Transfer Object")
public record TransferDTO (

        @DecimalMin("0.01")
        BigDecimal value,

        @NotNull
        @Schema(example = "38")
        Long payerId,

        @NotNull
        @Schema(example = "18")
        Long payeeId

) {}

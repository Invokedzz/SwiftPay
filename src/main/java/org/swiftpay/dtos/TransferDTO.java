package org.swiftpay.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferDTO (

        @DecimalMin("0.01")
        BigDecimal value,

        @NotNull
        Long payerId,

        @NotNull
        Long payeeId

) {}

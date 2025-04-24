package org.swiftpay.dtos;

import java.math.BigDecimal;

public record TransferRequestDTO (

        BigDecimal value,

        String walletId,

        String externalReference

) {}

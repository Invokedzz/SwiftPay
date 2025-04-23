package org.swiftpay.dtos;

import org.swiftpay.model.enums.TransferStatus;
import org.swiftpay.model.enums.TransferType;

import java.math.BigDecimal;

public record TransferResponseDTO (

        String id,

        TransferType type,

        BigDecimal value,

        TransferStatus status,

        PayeeDTO account

) {}

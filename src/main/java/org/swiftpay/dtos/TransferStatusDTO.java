package org.swiftpay.dtos;

import org.swiftpay.model.enums.TransferStatus;
import org.swiftpay.model.enums.TransferType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferStatusDTO (BigDecimal value, TransferType type, TransferStatus status, LocalDate transferDate) {

    public TransferStatusDTO (TransferStatusDTO transferStatusDTO) {

        this (transferStatusDTO.value, transferStatusDTO.type, transferStatusDTO.status, transferStatusDTO.transferDate);

    }

}

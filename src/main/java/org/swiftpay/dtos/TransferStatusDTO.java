package org.swiftpay.dtos;

import org.swiftpay.model.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferStatusDTO (BigDecimal value, String type, TransferStatus status, LocalDate dateCreated) {}

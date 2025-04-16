package org.swiftpay.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransferResponseDTO (String id, String status, BigDecimal value, LocalDate effectiveDate, String transactionType) {}

package org.swiftpay.dtos;

import java.math.BigDecimal;

public record BankTransferRequestDTO (BigDecimal value, BankDTO bankAccount) {}

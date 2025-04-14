package org.swiftpay.dtos;

import org.swiftpay.model.User;

import java.math.BigDecimal;

public record TransferDTO (BigDecimal value, Long payerId, Long payeeId) {}

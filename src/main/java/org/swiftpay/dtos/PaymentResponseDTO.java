package org.swiftpay.dtos;

import java.math.BigDecimal;

public record PaymentResponseDTO(String id, String status, BigDecimal value) {}

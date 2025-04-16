package org.swiftpay.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentRequestDTO (String customer, BigDecimal value, String billingType, LocalDate dueDate) {}

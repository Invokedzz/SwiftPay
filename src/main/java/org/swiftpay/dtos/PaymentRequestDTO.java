package org.swiftpay.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequestDTO (String customer, BigDecimal value, String billingType, LocalDate dueDate) {}

package org.swiftpay.dtos;

import java.math.BigDecimal;

public record TransferResponseDTO (String id, String type, BigDecimal value, String status, PayeeDTO account) {}

package org.swiftpay.dtos;

import java.time.LocalDate;

public record CreateChatDTO (String title, LocalDate createdAt, Long userId) {}

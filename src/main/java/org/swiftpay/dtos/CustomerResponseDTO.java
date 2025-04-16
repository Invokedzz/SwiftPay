package org.swiftpay.dtos;

import java.time.LocalDate;

public record CustomerResponseDTO (String id, String name, String email, String cpfCnpj, LocalDate dateCreated, String walletId) {}

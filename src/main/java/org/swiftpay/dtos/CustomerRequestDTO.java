package org.swiftpay.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerRequestDTO (String name, String email, String cpfCnpj, LocalDate birthDate, BigDecimal incomeValue, String postalCode) {}

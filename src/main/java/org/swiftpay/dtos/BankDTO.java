package org.swiftpay.dtos;

public record BankDTO (String ownerName, String cpfCnpj, String agency, String account, String accountDigit) {}

package org.swiftpay.dtos;

public record CustomerResponseDTO (

        String id,

        String name,

        String email,

        String cpfCnpj,

        String walletId

) {}

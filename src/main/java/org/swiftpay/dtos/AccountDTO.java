package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftpay.model.User;

import java.math.BigDecimal;

@Schema(name = "Account Data Access Object", description = "For users that want to access their account. Without any worries.")
public record AccountDTO (

        @Schema(example = "Cloud")
        String username,

        @Schema(example = "Cloud@hotmail.com")
        String email,

        @Schema(example = "95861462003")
        String cpfCnpj,

        @Schema(example = "543.54")
        BigDecimal balance

    ) {

    public AccountDTO (User user) {

        this (user.getUsername(), user.getEmail(), user.getCpfCnpj(), user.getWallet().getBalance());

    }

}

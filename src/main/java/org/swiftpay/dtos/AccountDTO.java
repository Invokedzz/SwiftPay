package org.swiftpay.dtos;

import org.swiftpay.model.User;
import org.swiftpay.model.Wallet;

import java.math.BigDecimal;

public record AccountDTO (String username, String email, String cpfCnpj, BigDecimal balance) {

    public AccountDTO (User user) {

        this (user.getUsername(), user.getEmail(), user.getCpfCnpj(), user.getWallet().getBalance());

    }

}

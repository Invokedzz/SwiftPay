package org.swiftpay.dtos;

import org.swiftpay.model.User;

public record ViewAllUsersDTO (String username, String email, String cpfCnpj) {

    public ViewAllUsersDTO (User user) {

        this (user.getUsername(), user.getEmail(), user.getCpfCnpj());

    }

}

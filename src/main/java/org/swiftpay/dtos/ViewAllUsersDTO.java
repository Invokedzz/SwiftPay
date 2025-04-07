package org.swiftpay.dtos;

import org.swiftpay.model.Role;
import org.swiftpay.model.User;

import java.util.List;
import java.util.stream.Collectors;

public record ViewAllUsersDTO (Long id, String username, String email, String cpfCnpj, List<Long> roleId) {

    public ViewAllUsersDTO (User user) {

        this (user.getId(), user.getUsername(), user.getEmail(), user.getCpfCnpj(), user.getRoles().stream().map(Role::getId).collect(Collectors.toList()));

    }

}

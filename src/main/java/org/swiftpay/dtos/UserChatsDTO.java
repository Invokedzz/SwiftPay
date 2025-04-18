package org.swiftpay.dtos;

import java.time.LocalDate;

public record UserChatsDTO (String title, LocalDate createdAt) {

    public UserChatsDTO (UserChatsDTO userChatsDTO) {

        this (userChatsDTO.title, userChatsDTO.createdAt);

    }
}

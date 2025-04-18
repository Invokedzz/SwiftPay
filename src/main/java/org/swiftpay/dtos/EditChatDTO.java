package org.swiftpay.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditChatDTO (

        @NotBlank(message = "Title field cannot be blank!")
        @Length(min = 4, max = 100, message = "Title field length must be bigger than 4 and lower than 100")
        String title

    ) {}

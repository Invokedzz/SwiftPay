package org.swiftpay.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginDTO (

        @NotBlank(message = "Username field cannot be blank!")
        @Length(min = 3, max = 20, message = "Username field must contain 3 to 20 characters!")
        String username,

        @NotBlank(message = "Password field cannot be blank!")
        @Length(min = 6, max = 15, message = "Password field must contain 6 to 15 characters!")
        String password

) {}

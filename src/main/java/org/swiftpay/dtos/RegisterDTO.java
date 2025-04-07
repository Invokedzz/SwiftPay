package org.swiftpay.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegisterDTO(

        @NotBlank(message = "Username field cannot be blank!")
        @Length(min = 3, max = 20, message = "Username field must contain 3 to 20 characters!")
        String username,

        @Email(message = "Invalid Email. Please, try again!")
        @NotBlank(message = "Email field cannot be blank!")
        String email,

        String cpfCnpj,

        @NotBlank(message = "Password field cannot be blank!")
        @Length(min = 6, max = 15, message = "Password field must contain 6 to 15 characters!")
        String password

) {}
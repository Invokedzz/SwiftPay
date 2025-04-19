package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Register Data Transfer Object")
public record RegisterDTO(

        @NotBlank(message = "Username field cannot be blank!")
        @Schema(example = "Sephiroth", minLength = 3, maxLength = 20)
        @Length(min = 3, max = 20, message = "Username field must contain 3 to 20 characters!")
        String name,

        @Schema(example = "email@gmail.com", maxLength = 254)
        @Email(message = "Invalid Email. Please, try again!")
        @NotBlank(message = "Email field cannot be blank!")
        String email,

        @Schema(description = "Use CPF for client. CNPJ for seller.", example = "55894121027")
        String cpfCnpj,

        @NotBlank(message = "Password field cannot be blank!")
        @Length(min = 6, max = 15, message = "Password field must contain 6 to 15 characters!")
        @Schema(minLength = 6, maxLength = 15, example = "Coffee, please")
        String password,

        @Past
        @Schema(example = "2003-10-02")
        LocalDate birthDate,

        @NotNull
        @Schema(example = "2550", minLength = 2, maxLength = 7)
        BigDecimal incomeValue,

        @Schema(example = "79841350")
        @Length(min = 8, max = 8, message = "Enter a valid length for your CEP!")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Enter a valid CEP format (xxxxx-xxx or xxxxxxxx)!")
        String postalCode

        ) {}
package org.swiftpay.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(name = "Login Data Transfer Object")
public record LoginDTO (

        @NotBlank(message = "Username field cannot be blank!")
        @Length(min = 3, max = 20, message = "Username field must contain 3 to 20 characters!")
        @Schema(example = "I_Love_Cats", minLength = 3, maxLength = 20)
        String username,

        @NotBlank(message = "Password field cannot be blank!")
        @Length(min = 6, max = 15, message = "Password field must contain 6 to 15 characters!")
        @Schema(example = "Dogs_as_well", minLength = 6, maxLength = 15)
        String password

) {}

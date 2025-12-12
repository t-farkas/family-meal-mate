package com.farkas.familymealmate.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email(message = "{auth.email.notblank}")
        @NotBlank(message = "{auth.email.invalid}")
        String email,

        @NotBlank(message = "{auth.password.notblank}")
        String password) {
}

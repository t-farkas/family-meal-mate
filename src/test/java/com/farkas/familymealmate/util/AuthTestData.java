package com.farkas.familymealmate.util;

import com.farkas.familymealmate.model.dto.auth.LoginRequest;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;

import java.time.LocalDate;

public class AuthTestData {

    public static RegisterRequest validRegisterRequest() {
        return registerRequest("tim@example.com", "secret", "tim", LocalDate.of(1990, 5, 5));
    }

    public static LoginRequest validLoginRequest() {
        return new LoginRequest("tim@example.com", "secret");
    }

    public static LoginRequest invalidLoginRequest() {
        return new LoginRequest("john@example.com", "secret");
    }

    private static RegisterRequest registerRequest(String email, String password, String name, LocalDate dateOfBirth) {
        return RegisterRequest.builder()
                .email(email)
                .password(password)
                .familyMemberCreateRequest(
                        FamilyMemberCreateRequest.builder()
                                .name(name)
                                .dateOfBirth(dateOfBirth)
                                .build())
                .build();
    }

}

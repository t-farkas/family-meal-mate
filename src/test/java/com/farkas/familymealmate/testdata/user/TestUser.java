package com.farkas.familymealmate.testdata.user;

import com.farkas.familymealmate.model.dto.auth.LoginRequest;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;

import java.time.LocalDate;

public record TestUser(
        String email,
        String password,
        String name,
        LocalDate dateOfBirth
) {

    public RegisterRequest registerNewHousehold(String householdName) {
        RegisterRequest request = baseRegisterRequest();
        request.setHouseholdName(householdName);
        return request;
    }

    public RegisterRequest registerExistingHousehold(String joinId) {
        RegisterRequest request = baseRegisterRequest();
        request.setHouseholdJoinId(joinId);
        return request;
    }

    public LoginRequest login() {
        return new LoginRequest(email, password);
    }

    public LoginRequest loginInvalid() {
        return new LoginRequest("wrong-" + email, password);
    }

    private RegisterRequest baseRegisterRequest() {
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

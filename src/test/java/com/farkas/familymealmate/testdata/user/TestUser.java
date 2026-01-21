package com.farkas.familymealmate.testdata.user;

import com.farkas.familymealmate.model.dto.auth.LoginRequest;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.entity.household.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.household.HouseholdEntity;
import com.farkas.familymealmate.model.entity.household.UserEntity;

import java.time.LocalDate;

public record TestUser(
        Long id,
        String email,
        String password,
        String name,
        LocalDate dateOfBirth
) {

    public RegisterRequest registerNewHousehold() {
        RegisterRequest request = baseRegisterRequest();
        request.setHouseholdName(name + "'s household");
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

    public UserEntity getEntity() {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setFamilyMember(getFamilyMember());
        user.getFamilyMember().setHousehold(getHousehold());
        return user;
    }

    private FamilyMemberEntity getFamilyMember() {
        FamilyMemberEntity familyMember = new FamilyMemberEntity();
        familyMember.setName(name);
        familyMember.setId(id);
        familyMember.setDateOfBirth(dateOfBirth);
        return familyMember;
    }

    private HouseholdEntity getHousehold() {
        HouseholdEntity household = new HouseholdEntity();
        household.setId(id);
        household.setName(name + "'s household");
        return household;
    }


}

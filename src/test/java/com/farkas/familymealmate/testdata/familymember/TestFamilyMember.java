package com.farkas.familymealmate.testdata.familymember;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.enums.AllergyType;

import java.time.LocalDate;
import java.util.Set;

public record TestFamilyMember(
        String name,
        LocalDate dateOfBirth,
        Set<AllergyType> allergies
) {

    public FamilyMemberCreateRequest createRequest() {
        return FamilyMemberCreateRequest.builder()
                .name(name)
                .dateOfBirth(dateOfBirth)
                .allergies(allergies)
                .build();
    }
}

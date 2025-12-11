package com.farkas.familymealmate.model.dto.familymember;

import com.farkas.familymealmate.model.enums.AllergyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FamilyMemberCreateRequest {

    private String name;
    private LocalDate dateOfBirth;
    private Set<AllergyType> allergies;

}

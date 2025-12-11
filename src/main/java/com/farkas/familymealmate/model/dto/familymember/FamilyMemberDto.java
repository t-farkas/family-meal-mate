package com.farkas.familymealmate.model.dto.familymember;

import com.farkas.familymealmate.model.enums.AllergyType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class FamilyMemberDto {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Set<AllergyType> allergies;
}

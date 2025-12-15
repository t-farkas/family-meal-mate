package com.farkas.familymealmate.model.dto.familymember;

import com.farkas.familymealmate.model.dto.household.HouseholdDto;
import com.farkas.familymealmate.model.enums.AllergyType;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FamilyMemberDto {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private Set<AllergyType> allergies;
    private HouseholdDto household;
}

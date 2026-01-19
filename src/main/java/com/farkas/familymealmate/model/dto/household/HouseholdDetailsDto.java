package com.farkas.familymealmate.model.dto.household;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdDetailsDto {
    private String name;
    private String joinId;
    private List<FamilyMemberDto> members;
}

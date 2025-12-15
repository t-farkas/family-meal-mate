package com.farkas.familymealmate.model.dto.household;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseholdDto {
    private Long id;
    private String name;
    private String joinId;
    private List<HouseholdMemberDto> members;
}

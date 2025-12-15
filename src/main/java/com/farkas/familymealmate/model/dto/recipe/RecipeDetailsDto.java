package com.farkas.familymealmate.model.dto.recipe;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberListDto;
import com.farkas.familymealmate.model.dto.household.HouseholdMemberDto;
import com.farkas.familymealmate.model.dto.recipe.tag.TagDto;
import com.farkas.familymealmate.model.entity.TagEntity;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDetailsDto {
    private String title;
    private String description;
    private Integer totalTime;
    private Integer serves;
    private List<String> instructions;
    private List<String> notes;
    private HouseholdMemberDto household;
    private FamilyMemberListDto createdBy;
    private Set<TagDto> tags;
}

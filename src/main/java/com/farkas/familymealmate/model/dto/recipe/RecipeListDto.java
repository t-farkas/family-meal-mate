package com.farkas.familymealmate.model.dto.recipe;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListDto {

    private Long id;
    private String title;
    private String description;
    private FamilyMemberListDto createdBy;
}

package com.farkas.familymealmate.model.dto.recipe;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
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
    private FamilyMemberDto createdBy;
}

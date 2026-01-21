package com.farkas.familymealmate.model.dto.recipe;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientDto;
import com.farkas.familymealmate.model.enums.AllergyType;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDetailsDto {

    private Long id;
    private String title;
    private String description;
    private Integer totalTime;
    private Integer serves;
    private List<String> instructions;
    private List<RecipeIngredientDto> ingredients;
    private Set<String> notes;
    private FamilyMemberDto createdBy;
    private Set<String> tags;
    private Set <AllergyType> allergies;
}

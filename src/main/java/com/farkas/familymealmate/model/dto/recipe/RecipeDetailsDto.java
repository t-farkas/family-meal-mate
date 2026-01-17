package com.farkas.familymealmate.model.dto.recipe;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberListDto;
import com.farkas.familymealmate.model.dto.household.HouseholdDto;
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
    private List<String> notes;
    private HouseholdDto household;
    private FamilyMemberListDto createdBy;
    private Set<String> tags;
    private Set <AllergyType> allergies;
}

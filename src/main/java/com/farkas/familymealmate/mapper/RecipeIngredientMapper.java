package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientDto;
import com.farkas.familymealmate.model.entity.RecipeIngredientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeIngredientMapper {

    @Mapping(source = "ingredient.name", target = "ingredientName")
    RecipeIngredientDto toDto(RecipeIngredientEntity entity);

    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "recipe", ignore = true)
    RecipeIngredientEntity toEntity(RecipeIngredientCreateRequestDto dto);
}

package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.entity.MealSlotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MealSlotMapper {

    @Mapping(source = "recipe.id", target = "recipeId")
    @Mapping(source = "recipe.title", target = "recipeTitle")
    MealSlotDetailsDto toDto(MealSlotEntity entity);
}

package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.RecipeIngredientEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import com.farkas.familymealmate.model.enums.AllergyType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = RecipeIngredientMapper.class)
public interface RecipeMapper {

    @Mapping(target = "allergies", expression = "java(getAllergies(entity))")
    @Mapping(target = "tags", expression = "java(getTags(entity))")
    RecipeDetailsDto toRecipeDetails(RecipeEntity entity);
    List<RecipeListDto> toDtoList(List<RecipeEntity> entities);
    RecipeEntity toEntity(RecipeCreateRequest dto);

    default Set<AllergyType> getAllergies(RecipeEntity recipe) {
        return recipe.getIngredients().stream()
                .map(RecipeIngredientEntity::getIngredient)
                .flatMap(ingredient -> ingredient.getAllergies().stream())
                .collect(Collectors.toSet());
    }

    default Set<String> getTags(RecipeEntity recipe) {
        return recipe.getTags().stream()
                .map(TagEntity::getName)
                .collect(Collectors.toSet());
    }
}

package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = RecipeIngredientMapper.class)
public interface RecipeMapper {

    RecipeDetailsDto toRecipeDetails(RecipeEntity entity);
    List<RecipeListDto> toDtoList(List<RecipeEntity> entities);
    RecipeEntity toEntity(RecipeCreateRequest dto);
}

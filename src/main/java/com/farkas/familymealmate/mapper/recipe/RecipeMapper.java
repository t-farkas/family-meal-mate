package com.farkas.familymealmate.mapper.recipe;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = RecipeIngredientMapper.class)
public interface RecipeMapper {

    @Mapping(target = "allergies", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "instructions", ignore = true)
    RecipeDetailsDto toRecipeDetails(RecipeEntity entity);

    List<RecipeListDto> toDtoList(List<RecipeEntity> entities);

    @Mapping(target = "household", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    RecipeEntity toEntity(RecipeCreateRequest dto);


}

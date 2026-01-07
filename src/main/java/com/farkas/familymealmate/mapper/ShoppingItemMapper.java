package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemDto;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.IngredientCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShoppingItemMapper {

    @Mapping(source = "ingredient.id", target = "ingredientId")
    @Mapping(source = "ingredient.name", target = "name")
    @Mapping(target = "category", expression = "java(mapCategory(entity))")
    ShoppingItemDto toDto(ShoppingItemEntity entity);

    default IngredientCategory mapCategory(ShoppingItemEntity entity) {
        return entity.getIngredient() == null ? IngredientCategory.OTHER : entity.getIngredient().getCategory();
    }
}

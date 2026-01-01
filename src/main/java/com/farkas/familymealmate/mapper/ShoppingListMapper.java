package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = ShoppingItemMapper.class)
public interface ShoppingListMapper {

    ShoppingListDto toDto(ShoppingListEntity request);

}

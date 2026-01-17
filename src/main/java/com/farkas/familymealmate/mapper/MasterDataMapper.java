package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;
import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MasterDataMapper {

    IngredientDto toIngredientDto(IngredientEntity entity);
    TagDto toTagDto(TagEntity entity);
}

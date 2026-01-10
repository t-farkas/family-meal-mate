package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;
import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MasterDataMapper {

    List<IngredientDto> toIngredientDtoList(List<IngredientEntity> entities);
    List<TagDto> toTagDtoList(List<TagEntity> entities);
}

package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;
import com.farkas.familymealmate.model.entity.masterdata.IngredientEntity;
import com.farkas.familymealmate.model.entity.masterdata.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MasterDataMapper {

    IngredientDto toIngredientDto(IngredientEntity entity);

    TagDto toTagDto(TagEntity entity);

    Set<String> toTagSet(Set<TagEntity> entities);

    default String map(TagEntity tag) {
        return tag == null ? null : tag.getName();
    }
}

package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.household.HouseholdDetailsDto;
import com.farkas.familymealmate.model.dto.household.HouseholdDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HouseholdMapper {

    @Mapping(target = "members", source = "members")
    HouseholdDetailsDto toDetailsDto(HouseholdEntity entity);
    HouseholdDto toDto(HouseholdEntity entity);

}

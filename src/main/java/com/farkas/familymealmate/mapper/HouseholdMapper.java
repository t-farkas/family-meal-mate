package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.household.HouseholdDto;
import com.farkas.familymealmate.model.dto.household.HouseholdMemberDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HouseholdMapper {

    @Mapping(target = "members", source = "members")
    HouseholdDto toDto(HouseholdEntity request);
    List<HouseholdMemberDto> mapMembers(List<FamilyMemberEntity> members);

}

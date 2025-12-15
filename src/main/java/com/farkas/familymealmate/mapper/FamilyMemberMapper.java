package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDetailsDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = HouseholdMapper.class)
public interface FamilyMemberMapper {

    FamilyMemberEntity toEntity(FamilyMemberCreateRequest request);
    FamilyMemberDetailsDto toDto(FamilyMemberEntity entity);

}

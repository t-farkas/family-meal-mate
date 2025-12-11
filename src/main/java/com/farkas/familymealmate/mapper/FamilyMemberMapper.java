package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FamilyMemberMapper {

    FamilyMemberEntity toEntity(RegisterRequest request);
    FamilyMemberEntity toEntity(FamilyMemberCreateRequest request);
    FamilyMemberDto toDto(FamilyMemberEntity entity);

}

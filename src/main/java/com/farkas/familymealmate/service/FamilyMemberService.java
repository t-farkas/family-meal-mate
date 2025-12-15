package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;

public interface FamilyMemberService {

    FamilyMemberEntity createFamilyMember(FamilyMemberCreateRequest request, HouseholdEntity household);
    FamilyMemberDto addFamilyMember(FamilyMemberCreateRequest request);
    FamilyMemberDto getFamilyMember(Long id);
}

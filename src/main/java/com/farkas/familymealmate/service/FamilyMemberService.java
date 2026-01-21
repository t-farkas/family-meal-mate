package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDetailsDto;
import com.farkas.familymealmate.model.entity.household.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.household.HouseholdEntity;

public interface FamilyMemberService {

    FamilyMemberEntity createFamilyMember(FamilyMemberCreateRequest request, HouseholdEntity household);

    FamilyMemberDetailsDto addFamilyMember(FamilyMemberCreateRequest request);

    FamilyMemberDetailsDto getFamilyMember(Long id);
}

package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;

public interface FamilyMemberService {

    FamilyMemberEntity createFamilyMember(RegisterRequest request);
    void createFamilyMember(FamilyMemberCreateRequest request);
    FamilyMemberDto getFamilyMember(Long id);
}

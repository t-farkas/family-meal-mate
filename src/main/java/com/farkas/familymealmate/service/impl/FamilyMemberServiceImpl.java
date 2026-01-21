package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.household.FamilyMemberMapper;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDetailsDto;
import com.farkas.familymealmate.model.entity.household.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.household.HouseholdEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.HouseholdOwnedResourceType;
import com.farkas.familymealmate.repository.FamilyMemberRepository;
import com.farkas.familymealmate.security.CurrentUserHelper;
import com.farkas.familymealmate.security.annotation.CheckHouseholdAccess;
import com.farkas.familymealmate.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyMemberServiceImpl implements FamilyMemberService {

    private final FamilyMemberRepository repository;
    private final FamilyMemberMapper mapper;

    @Override
    public FamilyMemberEntity createFamilyMember(FamilyMemberCreateRequest request, HouseholdEntity household) {
        FamilyMemberEntity entity = mapper.toEntity(request);
        entity.setHousehold(household);
        return repository.save(entity);
    }

    @Override
    public FamilyMemberDetailsDto addFamilyMember(FamilyMemberCreateRequest request) {
        FamilyMemberEntity familyMember = createFamilyMember(request, CurrentUserHelper.getCurrentHousehold());
        return mapper.toDto(familyMember);
    }

    @Override
    @CheckHouseholdAccess(type = HouseholdOwnedResourceType.FAMILY_MEMBER)
    public FamilyMemberDetailsDto getFamilyMember(Long id) {

        FamilyMemberEntity entity = repository.findWithAllergiesAndHouseholdById(id)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.FAMILY_MEMBER_NOT_FOUND.format(id),
                        ErrorCode.FAMILY_MEMBER_NOT_FOUND));

        return mapper.toDto(entity);
    }
}

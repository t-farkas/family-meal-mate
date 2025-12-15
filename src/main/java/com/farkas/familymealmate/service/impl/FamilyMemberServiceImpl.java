package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.FamilyMemberMapper;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.FamilyMemberRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyMemberServiceImpl implements com.farkas.familymealmate.service.FamilyMemberService {

    private final FamilyMemberRepository repository;
    private final CurrentUserService currentUserService;
    private final FamilyMemberMapper mapper;

    @Override
    public FamilyMemberEntity createFamilyMember(FamilyMemberCreateRequest request, HouseholdEntity household) {
        FamilyMemberEntity entity = mapper.toEntity(request);
        entity.setHousehold(household);
        return repository.save(entity);
    }

    @Override
    public FamilyMemberDto addFamilyMember(FamilyMemberCreateRequest request) {
        FamilyMemberEntity familyMember = createFamilyMember(request, currentUserService.getCurrentHousehold());
        return mapper.toDto(familyMember);
    }

    @Override
    public FamilyMemberDto getFamilyMember(Long id) {
        FamilyMemberEntity entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.FAMILY_MEMBER_NOT_FOUND.format(id),
                        ErrorCode.FAMILY_MEMBER_NOT_FOUND));

        Long householdId = currentUserService.getCurrentHousehold().getId();

        if (!entity.getHousehold().getId().equals(householdId)) {
            throw new ServiceException(
                    ErrorCode.NO_AUTHORIZATION.format(id),
                    ErrorCode.NO_AUTHORIZATION);
        }

        return mapper.toDto(entity);
    }
}

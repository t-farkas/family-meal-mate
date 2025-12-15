package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.HouseholdMapper;
import com.farkas.familymealmate.model.dto.household.HouseholdDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.HouseholdRepository;
import com.farkas.familymealmate.security.CurrentUserService;
import com.farkas.familymealmate.service.HouseholdService;
import com.farkas.familymealmate.util.JoinIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HouseholdServiceImpl implements HouseholdService {

    private final CurrentUserService currentUserService;
    private final HouseholdMapper householdMapper;
    private final HouseholdRepository householdRepository;
    private final JoinIdGenerator joinIdGenerator;

    @Override
    public HouseholdEntity createHouseHold(String householdName) {
        HouseholdEntity entity = HouseholdEntity.builder()
                .name(householdName)
                .joinId(joinIdGenerator.generateUniqueJoinId())
                .build();

        return householdRepository.save(entity);
    }

    @Override
    public HouseholdEntity findByJoinId(String joinId) {
        return householdRepository.findByJoinId(joinId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.HOUSEHOLD_NOT_FOUND.format(joinId),
                        ErrorCode.HOUSEHOLD_NOT_FOUND));
    }

    @Override
    public HouseholdDto getCurrentHousehold() {
        FamilyMemberEntity memberEntity = currentUserService.getCurrentFamilyMember();
        HouseholdEntity currentHousehold = householdRepository.findByIdWithMembers(memberEntity.getHousehold().getId()).get();
        return householdMapper.toDto(currentHousehold);
    }
}

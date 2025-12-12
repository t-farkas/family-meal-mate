package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.FamilyMemberMapper;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.FamilyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyMemberServiceImpl implements com.farkas.familymealmate.service.FamilyMemberService {

    private final FamilyMemberRepository repository;
    private final FamilyMemberMapper mapper;

    @Override
    public FamilyMemberEntity createFamilyMember(FamilyMemberCreateRequest request) {
        FamilyMemberEntity entity = mapper.toEntity(request);
        return repository.save(entity);
    }

    @Override
    public FamilyMemberDto getFamilyMember(Long id) {
        FamilyMemberEntity entity = repository.findById(id)
                .orElseThrow(() -> new ServiceException(
                       ErrorCode.FAMILY_MEMBER_NOT_FOUND.format(id),
                        ErrorCode.FAMILY_MEMBER_NOT_FOUND));

        return mapper.toDto(entity);
    }
}

package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.mapper.FamilyMemberMapper;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.repository.FamilyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FamilyMemberService implements com.farkas.familymealmate.service.FamilyMemberService {

    private final FamilyMemberRepository repository;
    private final FamilyMemberMapper mapper;

    @Override
    public FamilyMemberEntity createFamilyMember(RegisterRequest request) {
        FamilyMemberEntity entity = mapper.toEntity(request);
        return repository.save(entity);
    }

    @Override
    public void createFamilyMember(FamilyMemberCreateRequest request) {
        FamilyMemberEntity entity = mapper.toEntity(request);
        repository.save(entity);
    }

    @Override
    public FamilyMemberDto getFamilyMember(Long id) {
        FamilyMemberEntity entity = repository.getReferenceById(id);
        return mapper.toDto(entity);
    }
}

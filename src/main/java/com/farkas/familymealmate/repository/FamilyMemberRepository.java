package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMemberEntity, Long> {


}

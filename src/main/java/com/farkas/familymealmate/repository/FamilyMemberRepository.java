package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.household.FamilyMemberEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends JpaRepository<FamilyMemberEntity, Long> {

    @EntityGraph(attributePaths = {"household", "allergies"})
    Optional<FamilyMemberEntity> findWithAllergiesAndHouseholdById(Long id);

}

package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseholdRepository extends JpaRepository<HouseholdEntity, Long> {

    boolean existsByJoinId(String joinId);
    Optional<HouseholdEntity> findByJoinId(String joinId);

    @Query("SELECT DISTINCT h FROM HouseholdEntity h LEFT JOIN FETCH h.members WHERE h.id = :id")
    Optional<HouseholdEntity> findByIdWithMembers(@Param("id") Long id);

}

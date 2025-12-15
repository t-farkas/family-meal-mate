package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    List<RecipeEntity> findAllByHouseholdId(Long householdId);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.tags t WHERE r.household.id = :householdId AND t.id IN :tagIds")
    List<RecipeEntity> findAllByHouseholdIdAndTagIds(@Param("householdId") Long householdId,
                                                     @Param("tagIds") Set<Long> tagIds);
}

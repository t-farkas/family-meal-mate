package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.MealPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlanEntity, Long> {

    Optional<MealPlanEntity> findByHouseholdIdAndWeekStart(Long householdId, LocalDate weekStart);
    long deleteByWeekStartBefore(LocalDate lastAllowedWeek);
    long countByHouseholdIdAndTemplate(Long householdIs, boolean template);
    List<MealPlanEntity> findAllByHouseholdIdAndTemplate(Long householdIs, boolean template);
    boolean existsByHouseholdIdAndWeekStart(Long householdId, LocalDate weekStart);
}

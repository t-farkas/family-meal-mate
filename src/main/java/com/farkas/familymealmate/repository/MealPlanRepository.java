package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.mealplan.MealPlanEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlanEntity, Long> {

    @EntityGraph(attributePaths = {"mealSlots", "mealSlots.recipe"})
    Optional<MealPlanEntity> findWithMealSlotsAndRecipesByHouseholdIdAndWeekStart(Long householdId, LocalDate weekStart);

    @EntityGraph(attributePaths = "mealSlots")
    Optional<MealPlanEntity> findWithMealSlotsByHouseholdIdAndWeekStart(Long householdId, LocalDate weekStart);

    Optional<MealPlanEntity> findByHouseholdIdAndWeekStart(Long householdId, LocalDate weekStart);

    long deleteByWeekStartBefore(LocalDate lastAllowedWeek);

    long countByHouseholdIdAndTemplate(Long householdIs, boolean template);

    List<MealPlanEntity> findAllByHouseholdIdAndTemplate(Long householdIs, boolean template);

    boolean existsByHouseholdIdAndWeekStart(Long householdId, LocalDate weekStart);
}

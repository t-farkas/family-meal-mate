package com.farkas.familymealmate.scheduling;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.repository.HouseholdRepository;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MealPlanCleanupScheduler {

    private final MealPlanService mealPlanService;
    private final HouseholdRepository householdRepository;

    @Scheduled(cron = "0 0 0 * * MON")
    public void renewMealPlans() {
        removeOld();
        createNewMealPlans();
    }

    private void removeOld() {
        LocalDate lastAllowedWeek = MealPlanDateUtils.getCurrentWeekStart();
        mealPlanService.cleanupBefore(lastAllowedWeek);
    }

    private void createNewMealPlans() {
        List<HouseholdEntity> households = householdRepository.findAll();
        households.forEach(mealPlanService::create);
    }

}

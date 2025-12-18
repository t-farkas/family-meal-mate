package com.farkas.familymealmate.scheduling;

import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MealPlanCleanupScheduler {

    private final MealPlanRepository mealPlanRepository;

    @Scheduled(cron = "0 0 0 * * MON")
    public void cleanupOldMealPlans() {
        LocalDate lastAllowedWeek = MealPlanDateUtils.getCurrentWeekStart();
        long deletedRows = mealPlanRepository.deleteByWeekStartBefore(lastAllowedWeek);

        if (deletedRows > 0){
            log.info("Deleted {} old meal plans", deletedRows);
        }
    }

}

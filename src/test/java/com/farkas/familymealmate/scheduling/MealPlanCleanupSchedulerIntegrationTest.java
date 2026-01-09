package com.farkas.familymealmate.scheduling;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.repository.HouseholdRepository;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.util.JoinIdGenerator;
import com.farkas.familymealmate.util.MealPlanDateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MealPlanCleanupSchedulerIntegrationTest {

    @Autowired
    private MealPlanCleanupScheduler mealPlanCleanupScheduler;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private JoinIdGenerator joinIdGenerator;

    @Test
    void cleanupDeletesOnlyPreviousWeeks() {
        HouseholdEntity household = saveHousehold();

        LocalDate currentWeek = MealPlanDateUtils.getCurrentWeekStart();
        LocalDate nextWeek = MealPlanDateUtils.getNextWeekStart();
        LocalDate previousWeek = currentWeek.minusWeeks(1);

        List<MealPlanEntity> mealPlans = getMealPlans(household, currentWeek, nextWeek, previousWeek);
        mealPlanRepository.saveAll(mealPlans);

        mealPlanCleanupScheduler.cleanupOldMealPlans();

        List<MealPlanEntity> remainingPlans = mealPlanRepository.findAll();
        assertThat(remainingPlans).extracting(MealPlanEntity::getWeekStart).contains(currentWeek, nextWeek);
    }

    private HouseholdEntity saveHousehold() {
        HouseholdEntity household = new HouseholdEntity();
        household.setName("Household");
        household.setJoinId(joinIdGenerator.generateUniqueJoinId());
        household = householdRepository.save(household);
        return household;
    }

    private List<MealPlanEntity> getMealPlans(HouseholdEntity household, LocalDate... dates) {
        return Arrays.stream(dates).map(
                date -> {
                    MealPlanEntity mealPlanEntity = new MealPlanEntity();
                    mealPlanEntity.setHousehold(household);
                    mealPlanEntity.setWeekStart(date);
                    mealPlanEntity.setTemplate(false);

                    return mealPlanEntity;
                }).collect(Collectors.toList());
    }


}

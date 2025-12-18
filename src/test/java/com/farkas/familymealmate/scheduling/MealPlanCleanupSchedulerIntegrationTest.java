package com.farkas.familymealmate.scheduling;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
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
    private TestUserFactory testUserFactory;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Test
    void cleanupDeletesOnlyPreviousWeeks(){
        UserEntity user = testUserFactory.registerWithNewHousehold(TestUsers.BERTHA);

        LocalDate currentWeek = MealPlanDateUtils.getCurrentWeekStart();
        LocalDate nextWeek = MealPlanDateUtils.getNextWeekStart();
        LocalDate previousWeek = currentWeek.minusWeeks(1);

        List<MealPlanEntity> mealPlans = getMealPlans(user.getFamilyMember().getHousehold(), currentWeek, nextWeek, previousWeek);
        mealPlanRepository.saveAll(mealPlans);

        mealPlanCleanupScheduler.cleanupOldMealPlans();

        List<MealPlanEntity> remainingPlans = mealPlanRepository.findAll();
        assertThat(remainingPlans).extracting(MealPlanEntity::getWeekStart).contains(currentWeek, nextWeek);
    }

    private List<MealPlanEntity> getMealPlans(HouseholdEntity household, LocalDate... dates){
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

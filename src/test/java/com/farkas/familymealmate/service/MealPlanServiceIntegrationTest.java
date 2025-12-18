package com.farkas.familymealmate.service;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MealPlanServiceIntegrationTest {

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private MealPlanRepository mealPlanRepository;


    @Test
    void shouldCreateMealPlansWhenNoneExist() {
        UserEntity bertha = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(bertha);

        mealPlanService.createMealPlans();

        List<MealPlanEntity> mealPlans = mealPlanRepository.findAll();
        assertThat(mealPlans.size()).isEqualTo(2);
        assertThat(mealPlans).allMatch(plan -> plan.getHousehold().getId().equals(bertha.getFamilyMember().getHousehold().getId()));
        assertThat(mealPlans).extracting(MealPlanEntity::getWeekStart).containsExactly(MealPlanDateUtils.getCurrentWeekStart(), MealPlanDateUtils.getNextWeekStart());
        assertThat(mealPlans).allMatch(mp -> !mp.isTemplate());

    }

    @Test
    void shouldNotCreateDuplicateMealPlans_ifTheyAlreadyExist() {
        UserEntity bertha = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(bertha);

        mealPlanService.createMealPlans();

        List<MealPlanEntity> mealPlans = mealPlanRepository.findAll();
        assertThat(mealPlans.size()).isEqualTo(2);

        mealPlanService.createMealPlans();
        List<MealPlanEntity> mealPlans2 = mealPlanRepository.findAll();

        assertThat(mealPlans2.size()).isEqualTo(2);
        assertThat(mealPlans.get(0)).isIn(mealPlans2);
        assertThat(mealPlans.get(1)).isIn(mealPlans2);

    }
}

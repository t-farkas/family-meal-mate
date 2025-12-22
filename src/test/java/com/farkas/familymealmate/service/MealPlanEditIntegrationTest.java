package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.mealplan.TestMealPlanHelper;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MealPlanEditIntegrationTest {

    private static final String OMLETTE_NOTE = "A nice omelette";
    private static final String OATMEAL_NOTE = "A nice oatmeal";
    private static final String SPAGHETTI_NOTE = "A nice spaghetti";
    private static final String UPDATED_SPAGHETTI_NOTE = "UPDATED: A nice spaghetti";

    @Autowired
    TestMealPlanHelper mealPlanBuilder;

    @Autowired
    private TestUserFactory userFactory;

    @Test
    void shouldEditMealSlots() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);

        MealPlanDetailsDto mealPlan = mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(OATMEAL_NOTE, DayOfWeek.MONDAY, MealType.BREAKFAST, TestRecipes.OATMEAL)
                .slot(OMLETTE_NOTE, DayOfWeek.TUESDAY, MealType.BREAKFAST, TestRecipes.OMLETTE)
                .slot(SPAGHETTI_NOTE, DayOfWeek.WEDNESDAY, MealType.LUNCH, TestRecipes.SPAGHETTI_BOLOGNESE)
                .persist();

        assertThat(mealPlan.mealSlots()).hasSize(3);
        assertThat(mealPlan.mealSlots()).extracting(MealSlotDetailsDto::note)
                .contains(SPAGHETTI_NOTE, OATMEAL_NOTE, OMLETTE_NOTE);


        mealPlan = mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(OATMEAL_NOTE, DayOfWeek.MONDAY, MealType.BREAKFAST, TestRecipes.OATMEAL)
                .slot(UPDATED_SPAGHETTI_NOTE, DayOfWeek.WEDNESDAY, MealType.LUNCH, TestRecipes.SPAGHETTI_BOLOGNESE)
                .persist();

        assertThat(mealPlan.mealSlots()).hasSize(2);
        assertThat(mealPlan.mealSlots()).extracting(MealSlotDetailsDto::note)
                .contains(OATMEAL_NOTE, UPDATED_SPAGHETTI_NOTE);
    }


}

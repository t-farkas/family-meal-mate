package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.mealplan.TestMealPlanBuilder;
import com.farkas.familymealmate.testdata.recipe.TestRecipeFactory;
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

    private static final String UPDATED_SPAGHETTI_NOTE = "Wow this is an updated note";
    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private TestRecipeFactory recipeFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Test
    void shouldEditMealSlots() {
        setupTestWithUserAndMealPlan();
        MealPlanDetailsDto mealPlan = mealPlanService.getMealPlan(MealPlanWeek.CURRENT);
        MealPlanUpdateRequest request = getUpdatedMealPlanRequest(mealPlan);

        mealPlan = mealPlanService.editMealPlan(request);

        assertThat(mealPlan.mealSlots()).hasSize(2);
        assertThat(mealPlan.mealSlots()).extracting(MealSlotDetailsDto::note)
                .contains(TestRecipes.OVERNIGHT_OATS.note(), UPDATED_SPAGHETTI_NOTE);
    }

    private MealPlanUpdateRequest getUpdatedMealPlanRequest(MealPlanDetailsDto mealPlan) {
        return mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(TestRecipes.OVERNIGHT_OATS.note(), DayOfWeek.MONDAY, MealType.BREAKFAST, mealPlan.mealSlots().get(0).recipeId())
                .slot(UPDATED_SPAGHETTI_NOTE, DayOfWeek.WEDNESDAY, MealType.LUNCH, mealPlan.mealSlots().get(2).recipeId())
                .build();

    }

    private void setupTestWithUserAndMealPlan() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);
        mealPlanService.createMealPlans();
        mealPlanService.editMealPlan(getMealPlanUpdateRequest());

    }

    private MealPlanUpdateRequest getMealPlanUpdateRequest() {
        Long oatsId = recipeFactory.createRecipe(TestRecipes.OVERNIGHT_OATS);
        Long omeletteId = recipeFactory.createRecipe(TestRecipes.VEGETABLE_OMELETTE);
        Long bologneseId = recipeFactory.createRecipe(TestRecipes.SPAGHETTI_BOLOGNESE);

        return mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(TestRecipes.OVERNIGHT_OATS.note(), DayOfWeek.MONDAY, MealType.BREAKFAST, oatsId)
                .slot(TestRecipes.VEGETABLE_OMELETTE.note(), DayOfWeek.TUESDAY, MealType.BREAKFAST, omeletteId)
                .slot(TestRecipes.SPAGHETTI_BOLOGNESE.note(), DayOfWeek.WEDNESDAY, MealType.LUNCH, bologneseId)
                .build();
    }


}

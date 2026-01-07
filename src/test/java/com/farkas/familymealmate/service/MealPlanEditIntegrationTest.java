package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.mealplan.TestMealNotes;
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
                .contains(TestMealNotes.OATMEAL_BREAKFAST, TestMealNotes.UPDATED_NOTE);
    }

    private void setupTestWithUserAndMealPlan() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);
        mealPlanService.createMealPlans();
        mealPlanService.editMealPlan(getMealPlanUpdateRequest());

    }

    private MealPlanUpdateRequest getMealPlanUpdateRequest() {
        RecipeEntity oatsEntity = recipeFactory.createRecipe(TestRecipes.OVERNIGHT_OATS);
        RecipeEntity omeletteEntity = recipeFactory.createRecipe(TestRecipes.VEGETABLE_OMELETTE);
        RecipeEntity bologneseEntity = recipeFactory.createRecipe(TestRecipes.SPAGHETTI_BOLOGNESE);

        return mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(TestMealNotes.OATMEAL_BREAKFAST, DayOfWeek.MONDAY, MealType.BREAKFAST, oatsEntity)
                .slot(TestMealNotes.OMELETTE_BREAKFAST, DayOfWeek.TUESDAY, MealType.BREAKFAST, omeletteEntity)
                .slot(TestMealNotes.BOLOGNESE_LUNCH, DayOfWeek.WEDNESDAY, MealType.LUNCH, bologneseEntity)
                .buildRequest();
    }

    private MealPlanUpdateRequest getUpdatedMealPlanRequest(MealPlanDetailsDto mealPlan) {
        RecipeEntity recipe_1 = recipeFactory.getEntity(mealPlan.mealSlots().get(0).recipeId());
        RecipeEntity recipe_2 = recipeFactory.getEntity(mealPlan.mealSlots().get(1).recipeId());

        return mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(TestMealNotes.OATMEAL_BREAKFAST, DayOfWeek.MONDAY, MealType.BREAKFAST, recipe_1)
                .slot(TestMealNotes.UPDATED_NOTE, DayOfWeek.WEDNESDAY, MealType.LUNCH, recipe_2)
                .buildRequest();

    }


}

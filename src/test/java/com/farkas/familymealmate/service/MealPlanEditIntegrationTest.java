package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.testdata.mealplan.TestMealSlot;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MealPlanEditIntegrationTest {

    public static final String OMLETTE_NOTE = "A nice omelette";
    public static final String OATMEAL_NOTE = "A nice oatmeal";
    private static final String SPAGHETTI_NOTE = "A nice spaghetti";
    private static final String UPDATED_SPAGHETTI_NOTE = "UPDATED: A nice spaghetti";
    @Autowired
    private MealPlanService mealPlanService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private TestUserFactory userFactory;

    @Test
    void shouldEditMealSlots() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);

        MealPlanUpdateRequest mealPlanRequest = getMealPlanUpdateRequest();

        mealPlanService.createMealPlans();
        MealPlanDetailsDto mealPlan = mealPlanService.editMealPlan(mealPlanRequest);

        assertThat(mealPlan.mealSlots()).hasSize(3);
        assertThat(mealPlan.mealSlots()).extracting(MealSlotDetailsDto::note)
                .contains(SPAGHETTI_NOTE, OATMEAL_NOTE, OMLETTE_NOTE);

        MealPlanUpdateRequest updatedRequest = getUpdatedRequest(mealPlanRequest);
        mealPlan = mealPlanService.editMealPlan(updatedRequest);

        assertThat(mealPlan.mealSlots()).hasSize(2);
        assertThat(mealPlan.mealSlots()).extracting(MealSlotDetailsDto::note)
                .contains(UPDATED_SPAGHETTI_NOTE);
    }

    private MealPlanUpdateRequest getMealPlanUpdateRequest() {
        RecipeDetailsDto oatmeal = recipeService.create(TestRecipes.OATMEAL.createRequest());
        RecipeDetailsDto omelette = recipeService.create(TestRecipes.OMLETTE.createRequest());
        RecipeDetailsDto spaghetti = recipeService.create(TestRecipes.SPAGHETTI_BOLOGNESE.createRequest());

        MealSlotUpdateRequest spaghettiSlot = new TestMealSlot(null, SPAGHETTI_NOTE, spaghetti.getId()).lunch();
        MealSlotUpdateRequest omeletteSlot = new TestMealSlot(null, OMLETTE_NOTE, omelette.getId()).breakfast();
        MealSlotUpdateRequest oatmealSlot = new TestMealSlot(null, OATMEAL_NOTE, oatmeal.getId()).breakfast();

        return new MealPlanUpdateRequest(MealPlanWeek.CURRENT, List.of(spaghettiSlot, omeletteSlot, oatmealSlot));
    }

    private MealPlanUpdateRequest getUpdatedRequest(MealPlanUpdateRequest updateRequest) {
        MealSlotUpdateRequest spaghettiSlot = updateRequest.mealSlots().get(0);
        MealSlotUpdateRequest updatedSpaghettiSlot = new TestMealSlot(spaghettiSlot.id(), UPDATED_SPAGHETTI_NOTE, spaghettiSlot.recipeId()).lunch();

        return new MealPlanUpdateRequest(MealPlanWeek.CURRENT,List.of(updatedSpaghettiSlot, updateRequest.mealSlots().get(2)));
    }
}

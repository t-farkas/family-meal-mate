package com.farkas.familymealmate.service.mealplan;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.service.MealPlanService;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class MealPlanOptimisticLockingTest {

    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestRecipeFactory recipeFactory;

    @Autowired
    private MealPlanService service;

    @Test
    void throwsOptimisticLockingException() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);

        RecipeEntity recipe = recipeFactory.createRecipe(TestRecipes.OVERNIGHT_OATS);

        MealPlanUpdateRequest request = mealPlanBuilder
                .version(0L)
                .forWeek(MealPlanWeek.CURRENT)
                .slot("First slot", DayOfWeek.MONDAY, MealType.LUNCH, recipe)
                .buildRequest();

        service.update(request);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> service.update(request))
                .satisfies(e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.MEAL_PLAN_VERSION_MISMATCH));

    }

}

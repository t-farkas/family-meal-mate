package com.farkas.familymealmate.service;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.dto.template.TemplateCreateRequest;
import com.farkas.familymealmate.model.dto.template.TemplateDto;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class TemplateServiceIntegrationTest {

    public static final String TEMPLATE_NAME = "My first template";
    private final TestMealPlanBuilder mealPlanBuilder = new TestMealPlanBuilder();

    @Autowired
    MealPlanService mealPlanService;
    @Autowired
    private TestRecipeFactory recipeFactory;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TestUserFactory userFactory;

    @Test
    void shouldCreateTemplateFromCurrentWeek() {
        setupTestWithUserAndMealPlan();

        TemplateCreateRequest createRequest = getTemplateCreateRequest();
        TemplateDto template = templateService.createTemplate(createRequest);

        assertThat(template).isNotNull();
        assertThat(template.templateName()).isEqualTo(createRequest.name());
        assertThat(template.mealSlots()).hasSize(3);
        assertThat(template.mealSlots()).extracting(
                MealSlotDetailsDto::note).contains(
                TestMealNotes.OATMEAL_BREAKFAST,
                TestMealNotes.OMELETTE_BREAKFAST,
                TestMealNotes.BOLOGNESE_LUNCH);
    }

    @Test
    void shouldRejectTemplateWithSameName() {
        setupTestWithUserAndMealPlan();

        TemplateCreateRequest createRequest = getTemplateCreateRequest();
        templateService.createTemplate(createRequest);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> templateService.createTemplate(createRequest))
                .satisfies(e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TEMPLATE_NAME_ALREADY_EXISTS));

    }

    @Test
    void shouldDeleteTemplate() {
        setupTestWithUserAndMealPlan();

        TemplateCreateRequest createRequest = getTemplateCreateRequest();
        TemplateDto template = templateService.createTemplate(createRequest);

        templateService.deleteTemplate(template.id());
        List<TemplateDto> list = templateService.listTemplates();
        assertThat(list).isEmpty();

    }

    @Test
    void cannotDeleteTemplateFromDifferentHousehold() {
        setupTestWithUserAndMealPlan();

        TemplateCreateRequest createRequest = getTemplateCreateRequest();
        TemplateDto template = templateService.createTemplate(createRequest);

        UserEntity tim = userFactory.registerWithNewHousehold(TestUsers.TIM);
        userFactory.authenticate(tim);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> templateService.deleteTemplate(template.id()))
                .satisfies(e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORIZATION));
    }

    private void setupTestWithUserAndMealPlan() {
        UserEntity bertha = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(bertha);
        mealPlanService.createMealPlans();

        RecipeEntity oatsEntity = recipeFactory.createRecipe(TestRecipes.OVERNIGHT_OATS);
        RecipeEntity omeletteEntity = recipeFactory.createRecipe(TestRecipes.VEGETABLE_OMELETTE);
        RecipeEntity bologneseEntity = recipeFactory.createRecipe(TestRecipes.SPAGHETTI_BOLOGNESE);

        MealPlanUpdateRequest request = mealPlanBuilder
                .forWeek(MealPlanWeek.CURRENT)
                .slot(TestMealNotes.OATMEAL_BREAKFAST, DayOfWeek.MONDAY, MealType.BREAKFAST, oatsEntity)
                .slot(TestMealNotes.OMELETTE_BREAKFAST, DayOfWeek.TUESDAY, MealType.BREAKFAST, omeletteEntity)
                .slot(TestMealNotes.BOLOGNESE_LUNCH, DayOfWeek.WEDNESDAY, MealType.LUNCH, bologneseEntity)
                .buildRequest();

        mealPlanService.editMealPlan(request);
    }

    private TemplateCreateRequest getTemplateCreateRequest() {
        return new TemplateCreateRequest(TEMPLATE_NAME, MealPlanWeek.CURRENT);
    }

}

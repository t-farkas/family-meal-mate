package com.farkas.familymealmate.service;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealSlotUpdateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.template.TemplateCreateRequest;
import com.farkas.familymealmate.model.dto.template.TemplateDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class TemplateServiceIntegrationTest {

    private static final String OMLETTE_NOTE = "A nice omelette";
    private static final String OATMEAL_NOTE = "A nice oatmeal";
    private static final String SPAGHETTI_NOTE = "A nice spaghetti";

    @Autowired
    private TemplateService templateService;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private TestUserFactory userFactory;

    @Test
    void shouldCreateTemplateFromCurrentWeek() {
        createMealPlans();
        TemplateCreateRequest createRequest = new TemplateCreateRequest("my first template", MealPlanWeek.CURRENT);
        TemplateDto template = templateService.createTemplate(createRequest);

        assertThat(template).isNotNull();
        assertThat(template.templateName()).isEqualTo(createRequest.name());
        assertThat(template.mealSlots()).hasSize(3);
        assertThat(template.mealSlots()).extracting(
                MealSlotDetailsDto::note).contains(OMLETTE_NOTE, OATMEAL_NOTE, SPAGHETTI_NOTE);
    }


    @Test
    void shouldRejectWhenTemplateLimitReached() {
        createMealPlans();
        TemplateCreateRequest createRequest = new TemplateCreateRequest("my first template", MealPlanWeek.CURRENT);
        templateService.createTemplate(createRequest);
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> templateService.createTemplate(createRequest))
                .satisfies(e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.TEMPLATE_NAME_ALREADY_EXISTS));

    }

    @Test
    void shouldDeleteTemplate() {
        createMealPlans();
        TemplateCreateRequest createRequest = new TemplateCreateRequest("my first template", MealPlanWeek.CURRENT);
        TemplateDto template = templateService.createTemplate(createRequest);

        templateService.deleteTemplate(template.id());
        List<TemplateDto> list = templateService.listTemplates();
        assertThat(list).isEmpty();

    }

    private void createMealPlans() {
        UserEntity bertha = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(bertha);
        mealPlanService.createMealPlans();
        mealPlanService.editMealPlan(getMealPlanUpdateRequest());
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
}

package com.farkas.familymealmate.service;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeFilterRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUser;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.testutil.AuthenticationUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class RecipeServiceIntegrationTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void clearSecurityContext() {
        AuthenticationUtil.clear();
    }

    @Test
    void checkHouseholdAccessThrowsWhenRecipeIsFromAnotherHousehold() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.TIM);
        userFactory.authenticate(user);
        RecipeDetailsDto recipeDetailsDto = recipeService.create(TestRecipes.OVERNIGHT_OATS.createRequest());

        UserEntity user2 = userFactory.registerWithNewHousehold(TestUsers.JOHN);
        userFactory.authenticate(user2);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> recipeService.get(recipeDetailsDto.getId()))
                .satisfies(ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORIZATION));
    }

    @Test
    void createRecipeUsesCurrentHouseholdAndUser() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.TIM);
        userFactory.authenticate(user);
        RecipeDetailsDto recipeDetailsDto = recipeService.create(TestRecipes.OVERNIGHT_OATS.createRequest());

        String recipeHouseholdName = recipeDetailsDto.getHousehold().getName();
        String currentUserHouseholdName = user.getFamilyMember().getHousehold().getName();

        assertThat(recipeHouseholdName).isEqualTo(currentUserHouseholdName);
        Long recipeCreatedById = recipeDetailsDto.getCreatedBy().getId();
        Long currentUserId = user.getFamilyMember().getId();

        assertThat(recipeCreatedById).isEqualTo(currentUserId);
    }

    @Test
    void listReturnsOnlyRecipesFromCurrentHousehold() {
        UserEntity user1 = createHouseholdAndRecipes(TestUsers.BERTHA, TestRecipes.OVERNIGHT_OATS, TestRecipes.SPAGHETTI_BOLOGNESE);
        createHouseholdAndRecipes(TestUsers.JOHN, TestRecipes.SPAGHETTI_BOLOGNESE);

        HouseholdEntity household = user1.getFamilyMember().getHousehold();

        userFactory.authenticate(user1);
        List<RecipeListDto> recipes = recipeService.list(new RecipeFilterRequest()).getContent();
        recipes.forEach(recipe -> {
            RecipeDetailsDto recipeDetailsDto = recipeService.get(recipe.getId());
            assertThat(recipeDetailsDto.getHousehold().getName()).isEqualTo(household.getName());
        });
    }

    @Test
    void createRecipePersistsIngredients() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.TIM);
        userFactory.authenticate(user);

        RecipeCreateRequest request = TestRecipes.OVERNIGHT_OATS.createRequest();
        RecipeDetailsDto recipeDetailsDto = recipeService.create(request);
        RecipeDetailsDto loaded = recipeService.get(recipeDetailsDto.getId());

        assertThat(loaded.getIngredients()).hasSize(TestRecipes.OVERNIGHT_OATS.ingredients().size());

        RecipeIngredientDto loadedIngredient = loaded.getIngredients().get(0);
        RecipeIngredientCreateRequestDto requestIngredient = request.getIngredients().get(0);

        assertThat(loadedIngredient.getMeasurement()).isEqualTo(requestIngredient.getMeasurement());
        assertThat(loadedIngredient.getQuantity()).isEqualTo(requestIngredient.getQuantity());
    }

    @Test
    void deletingRecipeDeletesRecipeIngredients() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.TIM);
        userFactory.authenticate(user);

        RecipeDetailsDto recipe = recipeService.create(TestRecipes.OVERNIGHT_OATS.createRequest());
        Long recipeId = recipe.getId();

        recipeService.delete(recipeId);

        entityManager.flush();
        entityManager.clear();

        assertThatExceptionOfType(ServiceException.class).isThrownBy(
                        () -> recipeService.get(recipeId))
                .satisfies(ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.RECIPE_NOT_FOUND));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM recipe_ingredient WHERE recipe_id = ?",
                Integer.class, recipeId);

        assertThat(count).isZero();
    }


    private UserEntity createHouseholdAndRecipes(TestUser user, TestRecipe... recipes) {
        UserEntity userEntity = userFactory.registerWithNewHousehold(user);
        userFactory.authenticate(userEntity);

        Arrays.stream(recipes).forEach(recipe -> recipeService.create(recipe.createRequest()));
        return userEntity;
    }

}

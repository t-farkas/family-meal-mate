package com.farkas.familymealmate.service;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUser;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.util.AuthenticationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @AfterEach
    void clearSecurityContext() {
        AuthenticationUtil.clear();
    }

    @Test
    void checkHouseholdAccessThrowsWhenRecipeIsFromAnotherHousehold() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.TIM);
        userFactory.authenticate(user);
        RecipeDetailsDto recipeDetailsDto = recipeService.create(TestRecipes.OATMEAL.createRequest());

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
        RecipeDetailsDto recipeDetailsDto = recipeService.create(TestRecipes.OATMEAL.createRequest());

        Long recipeHouseholdId = recipeDetailsDto.getHousehold().getId();
        Long currentUserHouseholdId = user.getFamilyMember().getHousehold().getId();

        assertThat(recipeHouseholdId).isEqualTo(currentUserHouseholdId);
        Long recipeCreatedById = recipeDetailsDto.getCreatedBy().getId();
        Long currentUserId = user.getFamilyMember().getId();

        assertThat(recipeCreatedById).isEqualTo(currentUserId);
    }

    @Test
    void listReturnsOnlyRecipesFromCurrentHousehold() {
        UserEntity user1 = createHouseholdAndRecipes(TestUsers.BERTHA, TestRecipes.OATMEAL, TestRecipes.SPAGHETTI_BOLOGNESE);
        createHouseholdAndRecipes(TestUsers.JOHN, TestRecipes.SPAGHETTI_BOLOGNESE);

        HouseholdEntity household = user1.getFamilyMember().getHousehold();

        userFactory.authenticate(user1);
        List<RecipeListDto> recipes = recipeService.list();
        recipes.forEach(recipe -> {
            RecipeDetailsDto recipeDetailsDto = recipeService.get(recipe.getId());
            assertThat(recipeDetailsDto.getHousehold().getId()).isEqualTo(household.getId());
        });

    }

    private UserEntity createHouseholdAndRecipes(TestUser user, TestRecipe... recipes) {
        UserEntity userEntity = userFactory.registerWithNewHousehold(user);
        userFactory.authenticate(userEntity);

        Arrays.stream(recipes).forEach(recipe -> recipeService.create(recipe.createRequest()));
        return userEntity;
    }

}

package com.farkas.familymealmate.security;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.service.RecipeService;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class RecipeSecurityAspectTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private TestUserFactory testUserFactory;

    @Test
    void annotatedMethodTriggersHouseholdSecurityAspect(){

        UserEntity user1 = testUserFactory.registerWithNewHousehold(TestUsers.TIM);
        UserEntity user2 = testUserFactory.registerWithNewHousehold(TestUsers.BERTHA);

        testUserFactory.authenticate(user2);
        RecipeDetailsDto recipe = recipeService.create(TestRecipes.OVERNIGHT_OATS.createRequest());

        testUserFactory.authenticate(user1);

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> recipeService.get(recipe.getId()))
                .satisfies(ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORIZATION));
    }
}

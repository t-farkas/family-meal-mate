package com.farkas.familymealmate.service.shoppinglist;

import com.farkas.familymealmate.mapper.shoppinglist.ShoppingListMapper;
import com.farkas.familymealmate.model.entity.mealplan.MealPlanEntity;
import com.farkas.familymealmate.model.entity.recipe.RecipeIngredientEntity;
import com.farkas.familymealmate.model.entity.shoppinglist.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.RecipeRepository;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.security.CustomUserDetails;
import com.farkas.familymealmate.service.MealPlanService;
import com.farkas.familymealmate.service.impl.ShoppingListServiceImpl;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.shoppingList.TestShoppingListBuilder;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.testutil.AuthenticationUtil;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ShoppingListAggregationRetryUnitTest {

    private final TestShoppingListBuilder shoppingListBuilder = new TestShoppingListBuilder();
    @Mock
    private ShoppingListRepository shoppingListRepository;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private MealPlanService mealPlanService;
    @Mock
    private ShoppingListMapper mapper;
    @InjectMocks
    private ShoppingListServiceImpl service;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "maxRetries", 3);
        AuthenticationUtil.authenticateAs(new CustomUserDetails(TestUsers.JOHN.getEntity()));
    }

    @Test
    void shouldRetryWhenOptimisticLockExceptionIsThrown() {
        List<TestRecipe> recipes = List.of(TestRecipes.SPAGHETTI_BOLOGNESE, TestRecipes.PANCAKES);


        ShoppingListEntity shoppingListEntity = getShoppingListEntity(recipes);
        Mockito.when(shoppingListRepository.findWithShoppingItemsAndIngredientsByHouseholdId(TestUsers.JOHN.id()))
                .thenReturn(Optional.of(shoppingListEntity));

        Mockito.when(mealPlanService.getFullEntity(MealPlanWeek.CURRENT))
                .thenReturn(new MealPlanEntity());

        Mockito.when(recipeRepository.findAllRecipeIngredientByRecipeId(Mockito.anySet()))
                .thenReturn(getRecipeIngredients(recipes));

        Mockito.when(shoppingListRepository.save(shoppingListEntity))
                        .thenThrow(new OptimisticLockException())
                                .thenAnswer(invocation -> invocation.getArgument(0));

        service.addMealPlan(MealPlanWeek.CURRENT);

        Mockito.verify(shoppingListRepository, Mockito.times(2)).save(shoppingListEntity);
        Assertions.assertNotNull(shoppingListEntity.getUpdatedAt());

    }

    private List<RecipeIngredientEntity> getRecipeIngredients(List<TestRecipe> recipes) {
        return recipes.stream()
                .flatMap(recipe -> recipe.getRecipeIngredients().stream())
                .toList();
    }

    private ShoppingListEntity getShoppingListEntity(List<TestRecipe> recipes) {

        return shoppingListBuilder
                .note("Weekend shopping List")
                .version(0L)
                .addRecipes(recipes.toArray(new TestRecipe[0]))
                .buildEntity();

    }
}

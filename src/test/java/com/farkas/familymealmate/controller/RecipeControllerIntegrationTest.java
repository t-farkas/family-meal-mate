package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerIntegrationTest extends ApiTestBase {

    @Test
    void createRecipeFailsWhenInstructionsAreEmpty() throws Exception {

        String token = registerAndLoginUser(TestUsers.BERTHA);

        RecipeCreateRequest request = TestRecipes.VEGETABLE_OMELETTE.createRequest();
        request.setInstructions(null);

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeFailsWhenInstructionsAreBlank() throws Exception {
        String token = registerAndLoginUser(TestUsers.BERTHA);

        RecipeCreateRequest request = TestRecipes.VEGETABLE_OMELETTE.createRequest();
        request.setInstructions(List.of(""));

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeFailsWhenIngredientsAreBlank() throws Exception {
        String token = registerAndLoginUser(TestUsers.BERTHA);

        RecipeCreateRequest request = TestRecipes.VEGETABLE_OMELETTE.createRequest();
        request.setIngredients(new ArrayList<>());

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeFailsWhenTitleIsBlank() throws Exception {
        String token = registerAndLoginUser(TestUsers.BERTHA);

        RecipeCreateRequest request = TestRecipes.VEGETABLE_OMELETTE.createRequest();
        request.setTitle("");

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void correctRequestReturnsRecipe() throws Exception {

        String token = registerAndLoginUser(TestUsers.BERTHA);
        RecipeCreateRequest request = TestRecipes.VEGETABLE_OMELETTE.createRequest();
        RecipeIngredientCreateRequestDto firstIngredient = request.getIngredients().get(0);

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.instructions[0]").value(request.getInstructions().get(0)))
                .andExpect(jsonPath("$.ingredients[0].quantity").value(firstIngredient.getQuantity()))
                .andExpect(jsonPath("$.ingredients[0].measurement").value(firstIngredient.getMeasurement().name()));
    }

}

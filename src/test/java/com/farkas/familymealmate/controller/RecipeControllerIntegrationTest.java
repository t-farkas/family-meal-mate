package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerIntegrationTest extends ApiTestBase {

    @Test
    void createRecipeFailsWhenInstructionsAreEmpty() throws Exception {

        String token = registerAndLoginUser(TestUsers.BERTHA);

        RecipeCreateRequest request = TestRecipes.OMLETTE.createRequest();
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

        RecipeCreateRequest request = TestRecipes.OMLETTE.createRequest();
        request.setInstructions(List.of(""));

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRecipeFailsWhenTitleIsBlank() throws Exception {
        String token = registerAndLoginUser(TestUsers.BERTHA);

        RecipeCreateRequest request = TestRecipes.OMLETTE.createRequest();
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
        RecipeCreateRequest request = TestRecipes.OMLETTE.createRequest();

        mockMvc.perform(post("/api/recipe")
                        .with(authenticated(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.instructions[0]").value(request.getInstructions().get(0)));
    }

}

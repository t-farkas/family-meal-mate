package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.dto.recipe.ingredient.RecipeIngredientCreateRequestDto;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;
import java.util.List;

public class TestRecipes {

    public static final TestRecipe OATMEAL = new TestRecipe(
            "Oatmeal",
            List.of("Mix everything adn cook"),
            List.of(new RecipeIngredientCreateRequestDto(1L, BigDecimal.valueOf(3), QuantitativeMeasurement.LITER, null)));

    public static final TestRecipe OMLETTE = new TestRecipe(
            "A nice omlette",
            List.of("Sautee some veggies", "Add eggs and cook"),
            List.of(new RecipeIngredientCreateRequestDto(1L, BigDecimal.valueOf(3), QuantitativeMeasurement.LITER, null)));

    public static final TestRecipe SPAGHETTI_BOLOGNESE = new TestRecipe(
            "Kid's Spaghetti Bolognese",
            List.of("Heat up a can of bolognese sauce", "cook spaghetti and mix"),
            List.of(new RecipeIngredientCreateRequestDto(1L, BigDecimal.valueOf(3), QuantitativeMeasurement.LITER, null)));

}

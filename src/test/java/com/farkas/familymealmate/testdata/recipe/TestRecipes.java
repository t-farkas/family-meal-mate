package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;
import java.util.List;

public class TestRecipes {

    public static final TestRecipe CHICKEN_STIR_FRY = new TestRecipe(
            "Chicken Stir Fry",
            List.of(
                    "Cut chicken into strips",
                    "Stir fry with vegetables",
                    "Season and serve"
            ),
            List.of(
                    new TestRecipeIngredient(11L, BigDecimal.valueOf(500), QuantitativeMeasurement.GRAM, null),
                    new TestRecipeIngredient(57L, BigDecimal.ONE, QuantitativeMeasurement.PIECE, null),
                    new TestRecipeIngredient(56L, BigDecimal.valueOf(2), QuantitativeMeasurement.CLOVE, null),
                    new TestRecipeIngredient(74L, BigDecimal.valueOf(2), QuantitativeMeasurement.TABLESPOON, null),
                    new TestRecipeIngredient(46L, BigDecimal.ONE, QuantitativeMeasurement.TEASPOON, null)
            )
    );

    public static final TestRecipe SPAGHETTI_BOLOGNESE = new TestRecipe(
            "Spaghetti Bolognese",
            List.of(
                    "Brown the beef",
                    "Add tomatoes and simmer",
                    "Cook spaghetti and combine"
            ),
            List.of(
                    new TestRecipeIngredient(14L, BigDecimal.valueOf(400), QuantitativeMeasurement.GRAM, null),
                    new TestRecipeIngredient(28L, BigDecimal.valueOf(500), QuantitativeMeasurement.GRAM, null),
                    new TestRecipeIngredient(84L, BigDecimal.ONE, QuantitativeMeasurement.CAN, null),
                    new TestRecipeIngredient(57L, BigDecimal.ONE, QuantitativeMeasurement.PIECE, null),
                    new TestRecipeIngredient(46L, BigDecimal.ONE, QuantitativeMeasurement.TEASPOON, null)
            )
    );

    public static final TestRecipe VEGETABLE_OMELETTE = new TestRecipe(
            "Vegetable Omelette",
            List.of(
                    "Beat eggs",
                    "Cook vegetables",
                    "Add eggs and fold"
            ),
            List.of(
                    new TestRecipeIngredient(10L, BigDecimal.valueOf(3), QuantitativeMeasurement.PIECE, null),
                    new TestRecipeIngredient(57L, BigDecimal.ONE, QuantitativeMeasurement.PIECE, null),
                    new TestRecipeIngredient(65L, BigDecimal.ONE, QuantitativeMeasurement.PIECE, null),
                    new TestRecipeIngredient(74L, BigDecimal.ONE, QuantitativeMeasurement.TABLESPOON, null)
            )
    );

    public static final TestRecipe OVERNIGHT_OATS = new TestRecipe(
            "Overnight Oats",
            List.of(
                    "Mix ingredients",
                    "Let soak overnight",
                    "Serve cold"
            ),
            List.of(
                    new TestRecipeIngredient(36L, BigDecimal.valueOf(80), QuantitativeMeasurement.GRAM, null),
                    new TestRecipeIngredient(1L, BigDecimal.valueOf(250), QuantitativeMeasurement.MILLILITER, null),
                    new TestRecipeIngredient(52L, BigDecimal.ONE, QuantitativeMeasurement.TEASPOON, null)
            )
    );

    public static final TestRecipe PANCAKES = new TestRecipe(
            "Simple Pancakes",
            List.of(
                    "Mix dry ingredients",
                    "Add wet ingredients",
                    "Cook on pan"
            ),
            List.of(
                    new TestRecipeIngredient(39L, BigDecimal.valueOf(200), QuantitativeMeasurement.GRAM, null),
                    new TestRecipeIngredient(10L, BigDecimal.valueOf(2), QuantitativeMeasurement.PIECE, null),
                    new TestRecipeIngredient(1L, BigDecimal.valueOf(300), QuantitativeMeasurement.MILLILITER, null),
                    new TestRecipeIngredient(3L, BigDecimal.valueOf(20), QuantitativeMeasurement.GRAM, null)
            )
    );


}

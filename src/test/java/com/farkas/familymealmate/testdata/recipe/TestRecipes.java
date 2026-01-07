package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;
import java.util.List;

public class TestRecipes {

    public static final TestRecipe CHICKEN_STIR_FRY = new TestRecipe(
            1L,
            "Chicken Stir Fry",
            List.of("A lovely Chicken Stir Fry"),
            List.of(
                    "Cut chicken into strips",
                    "Stir fry with vegetables",
                    "Season and serve"
            ),
            List.of(
                    new TestRecipeIngredient(11L, "Chicken Breast", BigDecimal.valueOf(500), QuantitativeMeasurement.GRAM, IngredientCategory.PROTEIN),
                    new TestRecipeIngredient(57L, "Tomato", BigDecimal.ONE, QuantitativeMeasurement.PIECE, IngredientCategory.PRODUCE),
                    new TestRecipeIngredient(50L, "Garlic", BigDecimal.valueOf(2), QuantitativeMeasurement.CLOVE, IngredientCategory.PRODUCE),
                    new TestRecipeIngredient(60L, "Bell Pepper", BigDecimal.valueOf(1), QuantitativeMeasurement.PIECE, IngredientCategory.PRODUCE),
                    new TestRecipeIngredient(46L, "Cumin", BigDecimal.ONE, QuantitativeMeasurement.TEASPOON, IngredientCategory.SPICES_AND_HERBS)
            )
    );

    public static final TestRecipe SPAGHETTI_BOLOGNESE = new TestRecipe(
            2L,
            "Spaghetti Bolognese",
            List.of("Best Spaghetti Bolognese"),
            List.of(
                    "Brown the beef",
                    "Add tomatoes and simmer",
                    "Cook spaghetti and combine"
            ),
            List.of(
                    new TestRecipeIngredient(14L, "Ground Beef", BigDecimal.valueOf(400), QuantitativeMeasurement.GRAM, IngredientCategory.PROTEIN),
                    new TestRecipeIngredient(28L, "Pasta", BigDecimal.valueOf(500), QuantitativeMeasurement.GRAM, IngredientCategory.GRAINS_AND_PASTA),
                    new TestRecipeIngredient(84L, "Canned Corn", BigDecimal.ONE, QuantitativeMeasurement.CAN, IngredientCategory.CANNED_AND_JARS),
                    new TestRecipeIngredient(57L, "Tomato", BigDecimal.ONE, QuantitativeMeasurement.PIECE, IngredientCategory.PRODUCE),
                    new TestRecipeIngredient(46L, "Cumin", BigDecimal.ONE, QuantitativeMeasurement.TEASPOON, IngredientCategory.SPICES_AND_HERBS)
            )
    );

    public static final TestRecipe VEGETABLE_OMELETTE = new TestRecipe(
            3L,
            "Vegetable Omelette",
            List.of("Omelette Omelette"),
            List.of(
                    "Beat eggs",
                    "Cook vegetables",
                    "Add eggs and fold"
            ),
            List.of(
                    new TestRecipeIngredient(10L, "Eggs", BigDecimal.valueOf(3), QuantitativeMeasurement.PIECE, IngredientCategory.PROTEIN),
                    new TestRecipeIngredient(57L, "Tomato", BigDecimal.ONE, QuantitativeMeasurement.PIECE, IngredientCategory.PRODUCE),
                    new TestRecipeIngredient(65L, "Broccoli", BigDecimal.ONE, QuantitativeMeasurement.PIECE, IngredientCategory.PRODUCE),
                    new TestRecipeIngredient(63L, "Spinach", QualitativeMeasurement.HANDFUL, IngredientCategory.PRODUCE)
            )
    );

    public static final TestRecipe OVERNIGHT_OATS = new TestRecipe(
            4L,
            "Overnight Oats",
            List.of("Tasty Overnight Oats"),
            List.of(
                    "Mix ingredients",
                    "Let soak overnight",
                    "Serve cold"
            ),
            List.of(
                    new TestRecipeIngredient(36L, "All Purpose Flour", BigDecimal.valueOf(80), QuantitativeMeasurement.GRAM, IngredientCategory.BAKERY),
                    new TestRecipeIngredient(1L, "Milk", BigDecimal.valueOf(250), QuantitativeMeasurement.MILLILITER, IngredientCategory.DAIRY),
                    new TestRecipeIngredient(52L, "Onion", BigDecimal.ONE, QuantitativeMeasurement.TEASPOON, IngredientCategory.PRODUCE)
            )
    );

    public static final TestRecipe PANCAKES = new TestRecipe(
            5L,
            "Simple Pancakes",
            List.of("Quick and Easy Pancakes"),
            List.of(
                    "Mix dry ingredients",
                    "Add wet ingredients",
                    "Cook on pan"
            ),
            List.of(
                    new TestRecipeIngredient(39L, "Sugar", BigDecimal.valueOf(200), QuantitativeMeasurement.GRAM, IngredientCategory.BAKERY),
                    new TestRecipeIngredient(10L, "Eggs", BigDecimal.valueOf(2), QuantitativeMeasurement.PIECE, IngredientCategory.PROTEIN),
                    new TestRecipeIngredient(1L, "Milk", BigDecimal.valueOf(300), QuantitativeMeasurement.MILLILITER, IngredientCategory.DAIRY),
                    new TestRecipeIngredient(3L, "Butter", BigDecimal.valueOf(20), QuantitativeMeasurement.GRAM, IngredientCategory.DAIRY)
            )
    );


}

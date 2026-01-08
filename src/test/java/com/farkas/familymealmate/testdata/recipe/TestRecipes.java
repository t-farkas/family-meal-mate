package com.farkas.familymealmate.testdata.recipe;

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
                    TestRecipeIngredients.STIR_FRY_CHICKEN_BREAST,
                    TestRecipeIngredients.STIR_FRY_TOMATO,
                    TestRecipeIngredients.STIR_FRY_BELL_PEPPER,
                    TestRecipeIngredients.STIR_FRY_GARLIC,
                    TestRecipeIngredients.STIR_FRY_CUMIN)
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
                    TestRecipeIngredients.BOLOGNESE_BEEF,
                    TestRecipeIngredients.BOLOGNESE_TOMATO,
                    TestRecipeIngredients.BOLOGNESE_CUMIN,
                    TestRecipeIngredients.BOLOGNESE_PASTA,
                    TestRecipeIngredients.BOLOGNESE_CANNED_CORN)
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
                    TestRecipeIngredients.OMELETTE_EGGS,
                    TestRecipeIngredients.OMELETTE_BROCCOLI,
                    TestRecipeIngredients.OMELETTE_SPINACH,
                    TestRecipeIngredients.OMELETTE_TOMATO)
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
                    TestRecipeIngredients.OATS_OATS,
                    TestRecipeIngredients.OATS_MILK,
                    TestRecipeIngredients.OATS_SUGAR)
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
                    TestRecipeIngredients.PANCAKE_BUTTER,
                    TestRecipeIngredients.PANCAKE_EGGS,
                    TestRecipeIngredients.PANCAKE_MILK,
                    TestRecipeIngredients.PANCAKE_SUGAR)
    );

    public static final TestRecipe VEGGIE_BOWL = new TestRecipe(
            6L,
            "Tomato Broccoli Bowl",
            List.of("Light vegetable bowl with rice"),
            List.of(
                    "Chop vegetables",
                    "Cook rice",
                    "Combine and season"
            ),
            List.of(
                    TestRecipeIngredients.VEGGIE_BOWL_RICE,
                    TestRecipeIngredients.VEGGIE_BOWL_TOMATO,
                    TestRecipeIngredients.VEGGIE_BOWL_BROCCOLI
            )
    );



}

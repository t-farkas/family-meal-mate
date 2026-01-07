package com.farkas.familymealmate.testdata.recipe;

import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;

public class TestRecipeIngredients {

    public static final Long MILK_ID = 1L;
    public static final Long BUTTER_ID = 3L;
    public static final Long EGGS_ID = 10L;
    public static final Long CHICKEN_ID = 11L;
    public static final Long GROUND_BEEF_ID = 14L;
    public static final Long PASTA_ID = 28L;
    public static final Long SUGAR_ID = 39L;
    public static final Long CUMIN_ID = 46L;
    public static final Long GARLIC_ID = 50L;
    public static final Long TOMATO_ID = 57L;
    public static final Long BELL_PEPPER_ID = 60L;
    public static final Long SPINACH_ID = 63L;
    public static final Long BROCCOLI_ID = 65L;
    public static final Long OATS_ID = 34L;
    public static final Long CANNED_CORN_ID = 84L;

    public static final TestRecipeIngredient STIR_FRY_CHICKEN_BREAST = new TestRecipeIngredient(
            CHICKEN_ID,
            "Chicken Breast",
            BigDecimal.valueOf(500),
            QuantitativeMeasurement.GRAM,
            IngredientCategory.PROTEIN
    );

    public static final TestRecipeIngredient STIR_FRY_TOMATO = new TestRecipeIngredient(
            TOMATO_ID,
            "Tomato",
            BigDecimal.ONE,
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient STIR_FRY_GARLIC = new TestRecipeIngredient(
            GARLIC_ID,
            "Garlic",
            BigDecimal.valueOf(2),
            QuantitativeMeasurement.CLOVE,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient STIR_FRY_BELL_PEPPER = new TestRecipeIngredient(
            BELL_PEPPER_ID,
            "Bell Pepper",
            BigDecimal.valueOf(1),
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient STIR_FRY_CUMIN = new TestRecipeIngredient(
            CUMIN_ID,
            "Cumin",
            BigDecimal.ONE,
            QuantitativeMeasurement.TEASPOON,
            IngredientCategory.SPICES_AND_HERBS);

    public static final TestRecipeIngredient BOLOGNESE_BEEF = new TestRecipeIngredient(
            GROUND_BEEF_ID,
            "Ground Beef",
            BigDecimal.valueOf(400),
            QuantitativeMeasurement.GRAM,
            IngredientCategory.PROTEIN);

    public static final TestRecipeIngredient BOLOGNESE_PASTA = new TestRecipeIngredient(
            PASTA_ID,
            "Pasta",
            BigDecimal.valueOf(500),
            QuantitativeMeasurement.GRAM,
            IngredientCategory.GRAINS_AND_PASTA);

    public static final TestRecipeIngredient BOLOGNESE_CANNED_CORN = new TestRecipeIngredient(
            CANNED_CORN_ID,
            "Canned Corn",
            BigDecimal.ONE,
            QuantitativeMeasurement.CAN,
            IngredientCategory.CANNED_AND_JARS);

    public static final TestRecipeIngredient BOLOGNESE_TOMATO = new TestRecipeIngredient(
            TOMATO_ID,
            "Tomato",
            BigDecimal.ONE,
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient BOLOGNESE_CUMIN = new TestRecipeIngredient(
            CUMIN_ID,
            "Cumin",
            BigDecimal.ONE,
            QuantitativeMeasurement.TEASPOON,
            IngredientCategory.SPICES_AND_HERBS);

    public static final TestRecipeIngredient OMELETTE_EGGS = new TestRecipeIngredient(
            EGGS_ID,
            "Eggs",
            BigDecimal.valueOf(3),
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PROTEIN);

    public static final TestRecipeIngredient OMELETTE_TOMATO = new TestRecipeIngredient(
            TOMATO_ID,
            "Tomato",
            BigDecimal.ONE,
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient OMELETTE_BROCCOLI = new TestRecipeIngredient(
            BROCCOLI_ID,
            "Broccoli",
            BigDecimal.ONE,
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient OMELETTE_SPINACH = new TestRecipeIngredient(
            SPINACH_ID,
            "Spinach",
            QualitativeMeasurement.HANDFUL,
            IngredientCategory.PRODUCE);

    public static final TestRecipeIngredient OATS_OATS = new TestRecipeIngredient(
            OATS_ID,
            "Oats",
            BigDecimal.valueOf(80),
            QuantitativeMeasurement.GRAM,
            IngredientCategory.GRAINS_AND_PASTA);

    public static final TestRecipeIngredient OATS_MILK = new TestRecipeIngredient(
            MILK_ID,
            "Milk",
            BigDecimal.valueOf(250),
            QuantitativeMeasurement.MILLILITER,
            IngredientCategory.DAIRY);

    public static final TestRecipeIngredient OATS_SUGAR = new TestRecipeIngredient(
            SUGAR_ID,
            "Sugar",
            BigDecimal.ONE,
            QuantitativeMeasurement.TEASPOON,
            IngredientCategory.BAKERY);

    public static final TestRecipeIngredient PANCAKE_SUGAR = new TestRecipeIngredient(
            SUGAR_ID,
            "Sugar",
            BigDecimal.valueOf(200),
            QuantitativeMeasurement.GRAM,
            IngredientCategory.BAKERY);

    public static final TestRecipeIngredient PANCAKE_EGGS = new TestRecipeIngredient(
            EGGS_ID,
            "Eggs",
            BigDecimal.valueOf(2),
            QuantitativeMeasurement.PIECE,
            IngredientCategory.PROTEIN);

    public static final TestRecipeIngredient PANCAKE_MILK = new TestRecipeIngredient(
            MILK_ID,
            "Milk",
            BigDecimal.valueOf(300),
            QuantitativeMeasurement.MILLILITER,
            IngredientCategory.DAIRY);

    public static final TestRecipeIngredient PANCAKE_BUTTER = new TestRecipeIngredient(
            BUTTER_ID,
            "Butter",
            BigDecimal.valueOf(20),
            QuantitativeMeasurement.GRAM,
            IngredientCategory.DAIRY);

}

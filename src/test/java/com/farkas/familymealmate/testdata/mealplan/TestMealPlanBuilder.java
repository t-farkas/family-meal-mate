package com.farkas.familymealmate.testdata.mealplan;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.entity.*;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;
import com.farkas.familymealmate.testdata.recipe.TestRecipeIngredient;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class TestMealPlanBuilder {

    private List<TestMealSlot> mealSlots = new ArrayList<>();
    private MealPlanWeek week;

    public TestMealPlanBuilder forWeek(MealPlanWeek week) {
        this.week = week;
        return this;
    }

    public TestMealPlanBuilder slot(String note, DayOfWeek day, MealType mealType, RecipeEntity recipe){
        slot(null, note, day, mealType, new TestRecipe(recipe));
        return this;
    }

    public TestMealPlanBuilder slot(String note, DayOfWeek day, MealType mealType, TestRecipe recipe) {
        slot(null, note, day, mealType, recipe);
        return this;
    }

    public TestMealPlanBuilder slot(Long id, String note, DayOfWeek day, MealType mealType, TestRecipe recipe) {
        TestMealSlot slot = new TestMealSlot(id, note, day, mealType, recipe);

        mealSlots.add(slot);
        return this;
    }

    public MealPlanUpdateRequest buildRequest() {
        TestMealPlan plan = new TestMealPlan(week, mealSlots);
        MealPlanUpdateRequest request = plan.updateRequest();
        reset();
        return request;
    }

    public MealPlanEntity buildEntity() {
        MealPlanEntity entity = new MealPlanEntity();
        entity.setMealSlots(new ArrayList<>());

        mealSlots.forEach(slot -> {
            MealSlotEntity slotEntity = new MealSlotEntity();
            slotEntity.setMealPlan(entity);
            slotEntity.setDay(slot.day());
            slotEntity.setId(slot.id());
            slotEntity.setNote(slot.note());
            slotEntity.setMealType(slot.type());
            slotEntity.setRecipe(mapRecipe(slot));

            entity.getMealSlots().add(slotEntity);
        });

        return entity;
    }

    private void reset() {
        this.mealSlots.clear();
        this.week = null;
    }

    private RecipeEntity mapRecipe(TestMealSlot slot){
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(slot.recipe().id());
        recipeEntity.setTitle(slot.recipe().title());
        recipeEntity.setNotes(slot.recipe().notes());
        recipeEntity.setIngredients(mapIngredients(slot.recipe().ingredients()));
        recipeEntity.setInstructions(slot.recipe().instructions());
        return recipeEntity;
    }

    private List<RecipeIngredientEntity> mapIngredients(List<TestRecipeIngredient> ingredients) {
        return ingredients.stream()
                .map(ingredient -> {
                    RecipeIngredientEntity recipeIngredient = new RecipeIngredientEntity();
                    recipeIngredient.setIngredient(getIngredient(ingredient));
                    recipeIngredient.setQuantity(ingredient.quantity());
                    recipeIngredient.setQuantitativeMeasurement(ingredient.quantitativeMeasurement());
                    recipeIngredient.setQualitativeMeasurement(ingredient.qualitativeMeasurement());

                    return recipeIngredient;
                }).toList();

    }

    private IngredientEntity getIngredient(TestRecipeIngredient ingredient) {
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(ingredient.ingredientId());
        ingredientEntity.setName(ingredient.ingredientName());
        ingredientEntity.setCategory(ingredient.category());

        return ingredientEntity;
    }

}

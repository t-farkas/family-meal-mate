package com.farkas.familymealmate.testdata.shoppingList;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingItemUpdateRequest;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.testdata.recipe.TestRecipe;

import java.util.ArrayList;
import java.util.List;

public class TestShoppingListBuilder {

    List<TestShoppingItem> items = new ArrayList<>();

    public TestShoppingListBuilder addRecipes(TestRecipe... recipes) {
        for (TestRecipe recipe : recipes) {
            addRecipeIngredients(recipe);
        }
        return this;
    }

    public TestShoppingListBuilder addRecipeIngredients(TestRecipe recipe) {
        recipe.ingredients().forEach(ingredient -> items.add(new TestShoppingItem(ingredient)));
        return this;
    }

    public TestShoppingListBuilder addFreeTextItem(TestFreeTextItem item) {
        items.add(new TestShoppingItem(item.name(), item.note()));
        return this;
    }

    public ShoppingListEntity buildEntity() {
        ShoppingListEntity shoppingList = new ShoppingListEntity();
        shoppingList.setShoppingItems(new ArrayList<>());

        items.forEach(item -> {

            ShoppingItemEntity entity = new ShoppingItemEntity();
            entity.setNote(item.note());
            entity.setName(item.name());
            entity.setChecked(item.checked());
            entity.setIngredient(getIngredientEntity(item));
            entity.setQuantity(item.quantity());
            entity.setQuantitativeMeasurement(item.quantitativeMeasurement());

            entity.setShoppingList(shoppingList);
            shoppingList.getShoppingItems().add(entity);
        });

        reset();
        return shoppingList;
    }

    public ShoppingListUpdateRequest buildUpdateRequest() {
        ShoppingListUpdateRequest request = new ShoppingListUpdateRequest();
        request.setShoppingItems(new ArrayList<>());

        items.forEach(item -> {
            ShoppingItemUpdateRequest itemUpdateRequest = new ShoppingItemUpdateRequest();
            itemUpdateRequest.setName(item.name());
            itemUpdateRequest.setNote(item.note());
            itemUpdateRequest.setChecked(item.checked());
            itemUpdateRequest.setIngredientId(item.ingredientId());
            itemUpdateRequest.setQuantity(item.quantity());
            itemUpdateRequest.setQuantitativeMeasurement(item.quantitativeMeasurement());

            request.getShoppingItems().add(itemUpdateRequest);
        });

        reset();
        return request;
    }

    public List<ShoppingItemEntity> buildAggregateRequest(){
        return this.buildEntity().getShoppingItems();
    }

    private IngredientEntity getIngredientEntity(TestShoppingItem item) {
        if (item.ingredientId() == null) {
            return null;
        }

        IngredientEntity entity = new IngredientEntity();
        entity.setId(item.ingredientId());
        entity.setName(item.name());
        return entity;

    }

    private void reset() {
        this.items.clear();
    }

}

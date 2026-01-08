package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

public interface ShoppingListService {
    void createForHousehold(HouseholdEntity houseHold);

    ShoppingListDto getShoppingList();

    ShoppingListDto update(ShoppingListUpdateRequest updateRequest);

    ShoppingListDto addMealPlan(MealPlanWeek week);

}

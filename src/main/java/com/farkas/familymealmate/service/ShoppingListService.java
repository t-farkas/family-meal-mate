package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.VersionDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

public interface ShoppingListService {

    void create(HouseholdEntity houseHold);

    ShoppingListDto get();

    ShoppingListDto update(ShoppingListUpdateRequest updateRequest);

    ShoppingListDto addMealPlan(MealPlanWeek week);

    VersionDto getVersion();

}

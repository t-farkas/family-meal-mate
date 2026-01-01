package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    @Override
    public void createForHousehold(HouseholdEntity household) {
        Optional<ShoppingListEntity> shoppingList = shoppingListRepository.findByHouseholdId(household.getId());

        if (shoppingList.isEmpty()) {
            ShoppingListEntity entity = new ShoppingListEntity();
            entity.setHousehold(household);
            shoppingListRepository.save(entity);
        }
    }

    @Override
    public ShoppingListDto getShoppingList() {
        return null;
    }

    @Override
    public ShoppingListDto update(ShoppingListUpdateRequest updateRequest) {
        return null;
    }

    @Override
    public ShoppingListDto addMealPlan(MealPlanWeek week) {
        return null;
    }
}

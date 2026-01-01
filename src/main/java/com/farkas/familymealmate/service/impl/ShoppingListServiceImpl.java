package com.farkas.familymealmate.service.impl;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.mapper.ShoppingListMapper;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.security.CurrentUserService;
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
    private final CurrentUserService currentUserService;
    private final ShoppingListMapper mapper;

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
        Long householdId = currentUserService.getCurrentHousehold().getId();
        ShoppingListEntity shoppingList = getShoppingListEntity(householdId);

        return mapper.toDto(shoppingList);

    }

    @Override
    public ShoppingListDto edit(ShoppingListUpdateRequest updateRequest) {
        return null;
    }

    @Override
    public ShoppingListDto addMealPlan(MealPlanWeek week) {
        return null;
    }

    private ShoppingListEntity getShoppingListEntity(Long householdId) {
        return shoppingListRepository.findByHouseholdId(householdId)
                .orElseThrow(() -> new ServiceException(ErrorCode.SHOPPING_LIST_NOT_FOUND.getTemplate(), ErrorCode.SHOPPING_LIST_NOT_FOUND));
    }
}

package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListDto;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.service.ShoppingListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-list")
@RequiredArgsConstructor
public class ShoppingListController {

    private final ShoppingListService service;

    @GetMapping
    public ResponseEntity<ShoppingListDto> getShoppingList() {
        ShoppingListDto shoppingList = service.getShoppingList();
        return ResponseEntity.ok(shoppingList);
    }

    @PutMapping
    public ResponseEntity<ShoppingListDto> editShoppingList(@RequestBody @Valid ShoppingListUpdateRequest updateRequest) {
        ShoppingListDto shoppingList = service.edit(updateRequest);
        return ResponseEntity.ok(shoppingList);
    }

    @PutMapping
    public ResponseEntity<ShoppingListDto> addMealPlan(@RequestParam MealPlanWeek week) {
        ShoppingListDto shoppingList = service.addMealPlan(week);
        return ResponseEntity.ok(shoppingList);
    }

}

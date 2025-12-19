package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.service.MealPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mealplan")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService service;

    @PostMapping
    public ResponseEntity<BaseResponse> createMealPlans() {
        service.createMealPlans();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Meal Plans created successfully"));
    }

    @GetMapping
    public ResponseEntity<MealPlanDetailsDto> getMealPlan(@RequestParam MealPlanWeek week) {
        MealPlanDetailsDto mealPlan = service.getMealPlan(week);
        return ResponseEntity.ok(mealPlan);
    }

    @PutMapping
    public ResponseEntity<MealPlanDetailsDto> editMealPlan(@RequestBody @Valid MealPlanUpdateRequest mealPlan) {
        MealPlanDetailsDto mealPlanDetailsDto = service.editMealPlan(mealPlan);
        return ResponseEntity.ok(mealPlanDetailsDto);
    }

}

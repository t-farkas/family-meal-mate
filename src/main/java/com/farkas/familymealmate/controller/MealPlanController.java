package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
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

    @GetMapping("/current")
    public ResponseEntity<MealPlanDetailsDto> getCurrentPlan() {
        MealPlanDetailsDto mealPlan = service.getCurrentWeek();
        return ResponseEntity.ok(mealPlan);
    }

    @GetMapping("/next")
    public ResponseEntity<MealPlanDetailsDto> getNextPlan() {
        MealPlanDetailsDto mealPlan = service.getNextWeek();
        return ResponseEntity.ok(mealPlan);
    }

    @PutMapping("/current")
    public ResponseEntity<MealPlanDetailsDto> updateCurrentPlan(@RequestBody @Valid MealPlanUpdateRequest mealPlan) {
        MealPlanDetailsDto mealPlanDetailsDto = service.editCurrentWeek(mealPlan);
        return ResponseEntity.ok(mealPlanDetailsDto);
    }

    @PutMapping("/next")
    public ResponseEntity<MealPlanDetailsDto> updateNextPlan(@RequestBody @Valid MealPlanUpdateRequest mealPlan) {
        MealPlanDetailsDto mealPlanDetailsDto = service.editNextWeek(mealPlan);
        return ResponseEntity.ok(mealPlanDetailsDto);
    }
}

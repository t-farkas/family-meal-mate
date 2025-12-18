package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mealplan")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService service;

    @PostMapping
    private ResponseEntity<BaseResponse> createMealPlans() {
        service.createMealPlans();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Meal Plans created successfully"));
    }

    @GetMapping("/current")
    private ResponseEntity<MealPlanDetailsDto> getCurrentPlan() {
        MealPlanDetailsDto mealPlan = service.getCurrentWeek();
        return ResponseEntity.ok(mealPlan);
    }

    @GetMapping("/next")
    private ResponseEntity<MealPlanDetailsDto> getNextPlan() {
        MealPlanDetailsDto mealPlan = service.getNextWeek();
        return ResponseEntity.ok(mealPlan);
    }
}

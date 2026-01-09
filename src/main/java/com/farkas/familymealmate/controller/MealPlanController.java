package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.VersionDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.mealplan.MealPlanUpdateRequest;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.service.MealPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mealplan")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService service;

    @GetMapping
    public ResponseEntity<MealPlanDetailsDto> getMealPlan(@RequestParam MealPlanWeek week) {
        MealPlanDetailsDto mealPlan = service.get(week);
        return ResponseEntity.ok(mealPlan);
    }

    @PutMapping
    public ResponseEntity<MealPlanDetailsDto> editMealPlan(@RequestBody @Valid MealPlanUpdateRequest mealPlan) {
        MealPlanDetailsDto mealPlanDetailsDto = service.update(mealPlan);
        return ResponseEntity.ok(mealPlanDetailsDto);
    }

    @GetMapping("/version")
    public ResponseEntity<VersionDto> getVersion(@RequestParam MealPlanWeek week){
        VersionDto version = service.getVersion(week);
        return ResponseEntity.ok(version);
    }

}

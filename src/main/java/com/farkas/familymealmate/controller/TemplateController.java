package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.mealplan.TemplateDto;
import com.farkas.familymealmate.model.enums.MealPlanWeek;
import com.farkas.familymealmate.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService service;


    @GetMapping
    public ResponseEntity<List<TemplateDto>> getFavourites() {
        List<TemplateDto> favourites = service.listFavourites();
        return ResponseEntity.ok(favourites);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> markFavourite(@RequestParam MealPlanWeek week) {
        service.markFavourite(week);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Meal plan marked template successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {

        service.deleteFavourite(id);
        return ResponseEntity.noContent().build();
    }
}

package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;
import com.farkas.familymealmate.model.enums.MealType;
import com.farkas.familymealmate.model.enums.Measurement;
import com.farkas.familymealmate.service.MasterDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/masterdata")
@RequiredArgsConstructor
public class MasterDataController {

    private final MasterDataService masterDataService;

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> getTags() {

        List<TagDto> tags = masterDataService.getTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<IngredientDto>> getIngredients() {

        List<IngredientDto> ingredients = masterDataService.getIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/meal-type")
    public ResponseEntity<List<MealType>> getMealTypes() {

        List<MealType> mealTypes = Arrays.stream(MealType.values()).toList();
        return ResponseEntity.ok(mealTypes);
    }

    @GetMapping("/measurement")
    public ResponseEntity<List<Measurement>> getQuantitativeMeasurements() {

        List<Measurement> measurements = Arrays.stream(Measurement.values()).toList();
        return ResponseEntity.ok(measurements);
    }


}

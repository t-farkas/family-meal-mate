package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.household.HouseholdDto;
import com.farkas.familymealmate.service.HouseholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/household")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;

    @GetMapping("/current")
    public ResponseEntity<HouseholdDto> getCurrentHousehold() {
        HouseholdDto currentHousehold = householdService.getCurrentHousehold();
        return ResponseEntity.ok(currentHousehold);
    }
}

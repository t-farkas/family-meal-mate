package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.household.HouseholdDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;

public interface HouseholdService {

    HouseholdEntity createHouseHold(String householdName);
    HouseholdEntity findByJoinId(String joinId);
    HouseholdDto getCurrentHousehold();

}

package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.household.HouseholdDetailsDto;
import com.farkas.familymealmate.model.entity.HouseholdEntity;

public interface HouseholdService {

    HouseholdEntity createHouseHold(String householdName);

    HouseholdEntity findByJoinId(String joinId);

    HouseholdDetailsDto getCurrentHousehold();

}

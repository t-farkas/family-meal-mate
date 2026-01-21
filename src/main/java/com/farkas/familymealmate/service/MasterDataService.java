package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;

import java.util.Map;

public interface MasterDataService {

    Map<Long, TagDto> getTags();

    Map<Long, IngredientDto> getIngredients();

}

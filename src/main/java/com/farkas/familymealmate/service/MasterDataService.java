package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.masterdata.IngredientDto;
import com.farkas.familymealmate.model.dto.masterdata.TagDto;

import java.util.List;

public interface MasterDataService {

    List<TagDto> getTags();

    List<IngredientDto> getIngredients();
}

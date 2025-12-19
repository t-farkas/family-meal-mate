package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.mealplan.TemplateDto;
import com.farkas.familymealmate.model.enums.MealPlanWeek;

import java.util.List;

public interface TemplateService {

    void markFavourite(MealPlanWeek week);

    void deleteFavourite(Long id);

    List<TemplateDto> listFavourites();
}

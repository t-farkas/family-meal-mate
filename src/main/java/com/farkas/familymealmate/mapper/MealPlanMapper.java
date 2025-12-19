package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.mealplan.MealPlanDetailsDto;
import com.farkas.familymealmate.model.dto.template.TemplateDto;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = MealSlotMapper.class)
public interface MealPlanMapper {

    MealPlanDetailsDto toDto(MealPlanEntity request);

    TemplateDto toTemplateDto(MealPlanEntity request);

    List<TemplateDto> toTemplateDtoList(List<MealPlanEntity> request);

}

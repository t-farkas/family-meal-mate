package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.template.TemplateCreateRequest;
import com.farkas.familymealmate.model.dto.template.TemplateDto;

import java.util.List;

public interface TemplateService {

    void createTemplate(TemplateCreateRequest week);

    void deleteFavourite(Long id);

    List<TemplateDto> listFavourites();
}

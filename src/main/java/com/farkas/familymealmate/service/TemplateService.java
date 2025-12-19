package com.farkas.familymealmate.service;

import com.farkas.familymealmate.model.dto.template.TemplateCreateRequest;
import com.farkas.familymealmate.model.dto.template.TemplateDto;

import java.util.List;

public interface TemplateService {

    TemplateDto createTemplate(TemplateCreateRequest week);

    void deleteTemplate(Long id);

    List<TemplateDto> listTemplates();
}

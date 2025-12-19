package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.template.TemplateCreateRequest;
import com.farkas.familymealmate.model.dto.template.TemplateDto;
import com.farkas.familymealmate.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService service;


    @GetMapping
    public ResponseEntity<List<TemplateDto>> getFavourites() {
        List<TemplateDto> favourites = service.listTemplates();
        return ResponseEntity.ok(favourites);
    }

    @PostMapping
    public ResponseEntity<TemplateDto> createTemplate(@RequestBody TemplateCreateRequest request) {
        TemplateDto template = service.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(template);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {

        service.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}

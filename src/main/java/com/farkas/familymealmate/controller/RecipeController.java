package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.PagingResponse;
import com.farkas.familymealmate.model.dto.recipe.RecipeCreateRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.dto.recipe.RecipeFilterRequest;
import com.farkas.familymealmate.model.dto.recipe.RecipeListDto;
import com.farkas.familymealmate.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService service;

    @PostMapping
    public ResponseEntity<RecipeDetailsDto> createRecipe(@RequestBody @Valid RecipeCreateRequest request) {

        RecipeDetailsDto recipeDetailsDto = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeDetailsDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<RecipeDetailsDto> getRecipe(@PathVariable Long id) {

        RecipeDetailsDto recipe = service.get(id);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping
    public ResponseEntity<PagingResponse<RecipeListDto>> getRecipes(@ParameterObject RecipeFilterRequest request) {
        PagingResponse<RecipeListDto> list = service.list(request);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}


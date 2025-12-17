package com.farkas.familymealmate.model.dto.recipe;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeFilterRequest {

    private String name;
    private Set<Long> tagIds;
    private Set<Long> ingredientIds;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int pageSize = 20;

}

package com.farkas.familymealmate.model.dto.recipe;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeCreateRequest {
    private String title;
    private String description;
    private Integer totalTime;
    private Integer serves;
    private List<String> instructions;
    private List<String> notes;
    private Set<Long> tagIds;

}

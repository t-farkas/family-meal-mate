package com.farkas.familymealmate.model.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeCreateRequest {

    @NotBlank(message = "{recipe.title.notblank}")
    private String title;
    private String description;
    private Integer totalTime;
    private Integer serves;

    @NotEmpty(message = "{recipe.instructions.notempty}")
    private List< @NotBlank String> instructions;
    private List<String> notes;
    private Set<Long> tagIds;

}

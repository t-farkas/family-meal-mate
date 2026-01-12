package com.farkas.familymealmate.mapper;

import com.farkas.familymealmate.model.dto.recipe.RecipeDetailsDto;
import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.RecipeIngredientEntity;
import com.farkas.familymealmate.model.enums.AllergyType;
import com.farkas.familymealmate.model.enums.Measurement;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RecipeMapperTest {

    private final RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);
    private final RecipeIngredientMapper ingredientMapper = Mappers.getMapper(RecipeIngredientMapper.class);

    @Test
    void mapsAllergies() {

        ReflectionTestUtils.setField(mapper, "recipeIngredientMapper", ingredientMapper);

        IngredientEntity milk = getIngredientEntity("Milk", Set.of(AllergyType.DAIRY));
        IngredientEntity almond = getIngredientEntity("Almonds", Set.of(AllergyType.NUTS));

        RecipeIngredientEntity ri1 = getRecipeIngredientEntity(milk, BigDecimal.valueOf(200), Measurement.GRAM);
        RecipeIngredientEntity ri2 = getRecipeIngredientEntity(almond, BigDecimal.valueOf(50), Measurement.GRAM);

        RecipeEntity recipe = getRecipe(ri1, ri2);

        RecipeDetailsDto dto = mapper.toRecipeDetails(recipe);

        assertThat(dto.getIngredients()).hasSize(2);
        assertThat(dto.getAllergies())
                .contains(AllergyType.DAIRY, AllergyType.NUTS);

    }

    private RecipeEntity getRecipe(RecipeIngredientEntity ri1, RecipeIngredientEntity ri2) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setId(1L);
        recipe.setTitle("Test Recipe");
        recipe.setIngredients(List.of(ri1, ri2));
        return recipe;
    }

    private RecipeIngredientEntity getRecipeIngredientEntity(IngredientEntity milk, BigDecimal quantity, Measurement measurement) {
        RecipeIngredientEntity ri1 = new RecipeIngredientEntity();
        ri1.setIngredient(milk);
        ri1.setQuantity(quantity);
        ri1.setMeasurement(measurement);
        return ri1;
    }

    private IngredientEntity getIngredientEntity(String name, Set<AllergyType> allergies) {
        IngredientEntity milk = new IngredientEntity();
        milk.setName(name);
        milk.setAllergies(allergies);
        return milk;
    }
}

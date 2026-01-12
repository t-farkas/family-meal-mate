package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.*;
import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.model.enums.Measurement;
import com.farkas.familymealmate.repository.specification.RecipeSpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RecipeSpecificationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void filtersByHouseholdAndTag() {
        HouseholdEntity h1 = saveHousehold("Tim's household");
        HouseholdEntity h2 = saveHousehold("John's household");

        TagEntity vegan = saveTag("VEGAN");
        TagEntity spicy = saveTag("SPICY");

        saveRecipe("Vegan Tofu Curry", h1, Set.of(vegan, spicy));
        saveRecipe("Spicy Chicken", h1, Set.of(spicy));
        saveRecipe("Spicy Chili", h2, Set.of(spicy));

        entityManager.flush();

        Specification<RecipeEntity> spec = new RecipeSpecificationBuilder()
                .byHousehold(h1.getId())
                .byTagIds(Set.of(vegan.getId()))
                .build();

        List<RecipeEntity> recipes = recipeRepository.findAll(spec);
        assertThat(recipes.size()).isEqualTo(1);
        assertThat(recipes).extracting(RecipeEntity::getTitle).containsExactly("Vegan Tofu Curry");

    }

    @Test
    void filtersByIngredient() {
        HouseholdEntity h1 = saveHousehold("Bertha's household");

        IngredientEntity tofu = saveIngredient("Tofu");
        IngredientEntity chiliFlakes = saveIngredient("Chili flakes");
        IngredientEntity beans = saveIngredient("Beans");


        saveRecipe("Vegan Tofu Curry", h1, List.of(tofu, chiliFlakes));
        saveRecipe("Spicy Chili", h1, List.of(chiliFlakes, beans));

        entityManager.flush();

        Specification<RecipeEntity> spec = new RecipeSpecificationBuilder()
                .byHousehold(h1.getId())
                .byIngredientIds(Set.of(beans.getId()))
                .build();

        List<RecipeEntity> recipes = recipeRepository.findAll(spec);

        assertThat(recipes.size()).isEqualTo(1);
        assertThat(recipes).extracting(RecipeEntity::getTitle).containsExactly("Spicy Chili");

    }

    private TagEntity saveTag(String name) {
        TagEntity tag = new TagEntity();
        tag.setName(name);

        return entityManager.persist(tag);
    }

    private HouseholdEntity saveHousehold(String name) {
        HouseholdEntity h1 = new HouseholdEntity();
        h1.setJoinId(name.substring(0, 7));
        h1.setName(name);

        return entityManager.persist(h1);
    }

    private IngredientEntity saveIngredient(String name) {
        IngredientEntity ing = new IngredientEntity();
        ing.setName(name);
        ing.setCategory(IngredientCategory.OTHER);

        return entityManager.persist(ing);
    }

    private void saveRecipe(String title, HouseholdEntity household, Set<TagEntity> tags) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setTitle(title);
        recipe.setHousehold(household);
        recipe.setTags(tags);

        entityManager.persist(recipe);
    }

    private void saveRecipe(String title, HouseholdEntity household, List<IngredientEntity> ingredients) {
        RecipeEntity recipe = new RecipeEntity();
        recipe.setTitle(title);
        recipe.setHousehold(household);
        recipe.setIngredients(getIngredients(ingredients, recipe));

        entityManager.persist(recipe);
    }

    private List<RecipeIngredientEntity> getIngredients(List<IngredientEntity> ingredients, RecipeEntity recipe) {
        return ingredients.stream()
                .map(ingredient -> {
                    RecipeIngredientEntity recipeIngredient = new RecipeIngredientEntity();
                    recipeIngredient.setIngredient(ingredient);
                    recipeIngredient.setRecipe(recipe);
                    recipeIngredient.setMeasurement(Measurement.PINCH);

                    return recipeIngredient;
                }).collect(Collectors.toList());

    }
}

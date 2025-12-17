package com.farkas.familymealmate.init;

import com.farkas.familymealmate.model.entity.IngredientEntity;
import com.farkas.familymealmate.model.enums.AllergyType;
import com.farkas.familymealmate.model.enums.IngredientCategory;
import com.farkas.familymealmate.repository.IngredientRepository;
import com.farkas.familymealmate.util.CsvReaderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class IngredientDataLoader implements ApplicationRunner {

    private final IngredientRepository ingredientRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (ingredientRepository.count() == 0) {
            loadIngredients();
        }
    }

    private void loadIngredients() {
        List<IngredientEntity> ingredients = CsvReaderUtil.readCsvFile("/data/ingredients.csv", ",", getRowMapper());
        ingredientRepository.saveAll(ingredients);
        log.info("Loaded " + ingredients.size() + " ingredients");
    }

    private Function<String[], IngredientEntity> getRowMapper() {
        return row -> {
            IngredientEntity ingredient = new IngredientEntity();
            ingredient.setName(row[0].trim());
            ingredient.setCategory(IngredientCategory.valueOf(row[1].trim().toUpperCase()));

            String allergiesColumn = row.length > 2 ? row[2].trim() : "";
            Set<AllergyType> allergies = getAllergyTypes(allergiesColumn);
            ingredient.setAllergies(allergies);

            return ingredient;
        };
    }

    private Set<AllergyType> getAllergyTypes(String allergiesColumn) {
        return Arrays.stream(allergiesColumn.split("\\|"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(AllergyType::valueOf)
                .collect(Collectors.toSet());
    }

}

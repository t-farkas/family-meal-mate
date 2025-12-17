package com.farkas.familymealmate.repository.specification;

import com.farkas.familymealmate.model.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecipeSpecificationBuilder {

    private final List<Specification<RecipeEntity>> specifications = new ArrayList<>();

    public RecipeSpecificationBuilder byHousehold(Long householdId) {
        specifications.add(((root, query, cb) ->
                cb.equal(root.get(RecipeEntity_.household).get(HouseholdEntity_.id), householdId)));

        return this;
    }

    public RecipeSpecificationBuilder byName(String name) {
        if (name != null && !name.isEmpty()){
            specifications.add(((root, query, cb) ->
                    cb.like(cb.lower(root.get(RecipeEntity_.title)), "%" + name.toLowerCase() + "%")));
        }
        return this;
    }

    public RecipeSpecificationBuilder byTagIds(Set<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            specifications.add(((root, query, cb) -> {
                Join<RecipeEntity, TagEntity> tagJoin = root.join(RecipeEntity_.tags, JoinType.INNER);
                query.distinct(true);
                return tagJoin.get(TagEntity_.id).in(tagIds);
            }));
        }
        return this;
    }

    public RecipeSpecificationBuilder byIngredientIds(Set<Long> ingredientIds) {
        if (ingredientIds != null && !ingredientIds.isEmpty()) {
            specifications.add(((root, query, cb) -> {
                Join<RecipeEntity, RecipeIngredientEntity> ingredientJoin = root.join(RecipeEntity_.ingredients, JoinType.INNER);
                query.distinct(true);
                return ingredientJoin.get(RecipeIngredientEntity_.ingredient).get(IngredientEntity_.id).in(ingredientIds);
            }));
        }
        return this;
    }

    public Specification<RecipeEntity> build() {
        return specifications.stream()
                .reduce(Specification::and)
                .orElse(((root, query, cb) -> cb.conjunction()));
    }
}

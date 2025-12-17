package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.AllergyType;
import com.farkas.familymealmate.model.enums.IngredientCategory;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Ingredient")
@Table(name = "ingredient")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class IngredientEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private IngredientCategory category;

    @ElementCollection
    @CollectionTable(name = "ingredient_allergies", joinColumns = @JoinColumn(name = "ingredient_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "allergy")
    private Set<AllergyType> allergies;

    public Set<AllergyType> getAllergies() {
        if (allergies == null) {
            allergies = new HashSet<>();
        }
        return allergies;
    }
}

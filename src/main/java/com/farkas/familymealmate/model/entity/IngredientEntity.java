package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.AllergyType;
import com.farkas.familymealmate.model.enums.IngredientCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Ingredient")
@Table(name = "ingredient")
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

}

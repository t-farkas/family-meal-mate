package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.Measurement;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity(name = "RecipeIngredient")
@Table(name = "recipe_ingredient")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class RecipeIngredientEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeEntity recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private IngredientEntity ingredient;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "measurement", length = 50)
    private Measurement measurement;

}

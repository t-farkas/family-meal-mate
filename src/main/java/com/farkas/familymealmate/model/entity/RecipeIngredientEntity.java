package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
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
    @Column(name = "quantitative_measurement", length = 50)
    private QuantitativeMeasurement quantitativeMeasurement;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualitative_measurement", length = 100)
    private QualitativeMeasurement qualitativeMeasurement;
}

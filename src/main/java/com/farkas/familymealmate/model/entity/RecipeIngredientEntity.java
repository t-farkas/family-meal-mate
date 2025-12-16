package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import com.farkas.familymealmate.model.enums.QualitativeMeasurement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RecipeIngredient")
@Table(name = "recipe_ingredient")
public class RecipeIngredientEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private RecipeEntity recipe;

    @ManyToOne
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

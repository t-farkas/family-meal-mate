package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "shopping_item")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ShoppingItemEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id", nullable = false)
    private ShoppingListEntity shoppingList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private IngredientEntity ingredient;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "quantitative_measurement", length = 50)
    private QuantitativeMeasurement quantitativeMeasurement;

    private String name;
    private String note;
    private boolean checked;
}

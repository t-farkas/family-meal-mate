package com.farkas.familymealmate.model.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Measurement {

    GRAM(UnitType.WEIGHT, BigDecimal.ONE),
    DECAGRAM(UnitType.WEIGHT, BigDecimal.valueOf(100)),
    KILOGRAM(UnitType.WEIGHT, BigDecimal.valueOf(1000)),

    MILLILITER(UnitType.VOLUME, BigDecimal.ONE),
    DECILITER(UnitType.VOLUME, BigDecimal.valueOf(100)),
    LITER(UnitType.VOLUME, BigDecimal.valueOf(1000)),

    PIECE(UnitType.COUNT, BigDecimal.ONE),
    SLICE(UnitType.COUNT, BigDecimal.ONE),
    CLOVE(UnitType.COUNT, BigDecimal.ONE),
    CAN(UnitType.COUNT, BigDecimal.ONE),
    PACKAGE(UnitType.COUNT, BigDecimal.ONE),

    TEASPOON(UnitType.QUALITATIVE, BigDecimal.ONE),
    TABLESPOON(UnitType.QUALITATIVE, BigDecimal.ONE),

    PINCH(UnitType.QUALITATIVE, BigDecimal.ONE),
    SPLASH(UnitType.QUALITATIVE, BigDecimal.ONE),
    TO_TASTE(UnitType.QUALITATIVE, BigDecimal.ONE),
    HANDFUL(UnitType.QUALITATIVE, BigDecimal.ONE),
    AS_NEEDED(UnitType.QUALITATIVE, BigDecimal.ONE)
    ;

    private final BigDecimal baseFactor;
    private final UnitType unitType;

    Measurement(UnitType unitType, BigDecimal baseFactor) {
        this.unitType = unitType;
        this.baseFactor = baseFactor;
    }
}

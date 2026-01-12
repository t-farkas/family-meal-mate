package com.farkas.familymealmate.model.common;

import com.farkas.familymealmate.model.enums.Measurement;
import lombok.Getter;

import java.util.Objects;

@Getter
public class AggregationKey {

    private final Long ingredientId;
    private final Measurement unit;

    public AggregationKey(Long ingredientId, Measurement unit) {
        this.ingredientId = ingredientId;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AggregationKey that)) return false;
        return Objects.equals(ingredientId, that.ingredientId) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, unit);
    }
}

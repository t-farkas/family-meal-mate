package com.farkas.familymealmate.util;

import com.farkas.familymealmate.model.common.AggregationKey;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.Measurement;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class AggregationUtil {

    private AggregationUtil() {
    }

    public static void aggregateQuantities(BigDecimal value, Measurement from, ShoppingItemEntity item) {
        Measurement to = item.getMeasurement();

        if (to == null) {
            item.setQuantity(value);
            item.setMeasurement(from);
        } else {

            BigDecimal convertedValue = UnitConversionUtil.convert(value, from, to);
            item.setQuantity(item.getQuantity().add(convertedValue));
        }
    }

    public static Measurement autoMatchNull(Set<AggregationKey> aggregationKeys, ShoppingItemEntity item) {
        if (item.getMeasurement() != null) return item.getMeasurement();

        return aggregationKeys.stream()
                .filter(key -> key.getIngredientId().equals(item.getIngredient().getId()))
                .map(AggregationKey::getUnit)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static boolean canConvert(Measurement from, Measurement to) {
        return UnitConversionUtil.canConvert(from, to);
    }

    public static boolean isAggregatable(BigDecimal value, Measurement unit) {
        return isNotNull(value, unit) && UnitConversionUtil.shouldKeepQuantity(unit);
    }

    private static boolean isNotNull(BigDecimal value, Measurement unit) {
        return value != null && unit != null;
    }
}

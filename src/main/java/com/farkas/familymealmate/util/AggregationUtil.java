package com.farkas.familymealmate.util;

import com.farkas.familymealmate.model.common.AggregationKey;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class AggregationUtil {

    private AggregationUtil() {
    }

    public static void aggregateQuantities(BigDecimal value, QuantitativeMeasurement from, ShoppingItemEntity item) {
        QuantitativeMeasurement to = item.getQuantitativeMeasurement();

        if (to == null) {
            item.setQuantity(value);
            item.setQuantitativeMeasurement(from);
        } else {

            BigDecimal convertedValue = UnitConversionUtil.convert(value, from, to);
            item.setQuantity(item.getQuantity().add(convertedValue));
        }
    }

    public static QuantitativeMeasurement autoMatchNull(Set<AggregationKey> aggregationKeys, ShoppingItemEntity item) {
        if (item.getQuantitativeMeasurement() != null) return item.getQuantitativeMeasurement();

        return aggregationKeys.stream()
                .filter(key -> key.getIngredientId().equals(item.getIngredient().getId()))
                .map(AggregationKey::getUnit)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public static boolean canConvert(QuantitativeMeasurement from, QuantitativeMeasurement to) {
        return UnitConversionUtil.canConvert(from, to);
    }

    public static boolean isAggregatable(BigDecimal value, QuantitativeMeasurement unit) {
        return isNotNull(value, unit) && UnitConversionUtil.shouldKeepQuantity(unit);
    }

    private static boolean isNotNull(BigDecimal value, QuantitativeMeasurement unit) {
        return value != null && unit != null;
    }
}

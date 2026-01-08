package com.farkas.familymealmate.util;

import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.model.enums.QuantitativeMeasurement;

import java.math.BigDecimal;

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

    public static boolean canConvert(QuantitativeMeasurement from, QuantitativeMeasurement to) {
        return UnitConversionUtil.canConvert(from, to);
    }

    public static boolean isAggregatable(BigDecimal value, QuantitativeMeasurement unit) {
        return isQuantitative(value, unit) && UnitConversionUtil.shouldKeepQuantity(unit);
    }

    public static boolean isQuantitative(BigDecimal value, QuantitativeMeasurement unit) {
        return value != null && unit != null;
    }
}

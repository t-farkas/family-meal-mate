package com.farkas.familymealmate.util;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.Measurement;
import com.farkas.familymealmate.model.enums.UnitType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class UnitConversionUtil {

    private UnitConversionUtil() {
    }

    public static boolean shouldKeepQuantity(Measurement unit) {
        return unit.getUnitType() != UnitType.QUALITATIVE;
    }

    public static boolean canConvert(Measurement from, Measurement to) {
        if (from == null) return false;
        if (to == null) return true;
        return from.getUnitType() == to.getUnitType() && from.getUnitType() != UnitType.QUALITATIVE;
    }

    public static BigDecimal convert(BigDecimal value, Measurement from, Measurement to) {
        if (!canConvert(from, to)) {
            throw new ServiceException(
                    ErrorCode.INCOMPATIBLE_UNITS.format(from, to),
                    ErrorCode.INCOMPATIBLE_UNITS);
        }

        return value
                .multiply(from.getBaseFactor())
                .divide(to.getBaseFactor(), RoundingMode.HALF_UP);
    }


}

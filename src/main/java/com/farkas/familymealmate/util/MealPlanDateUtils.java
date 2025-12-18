package com.farkas.familymealmate.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class MealPlanDateUtils {

    private MealPlanDateUtils() {
    }

    public static LocalDate getCurrentWeekStart() {
        return LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static LocalDate getNextWeekStart() {
        return getCurrentWeekStart().plusWeeks(1);
    }

}

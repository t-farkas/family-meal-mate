package com.farkas.familymealmate.service.aggregation;

import com.farkas.familymealmate.model.common.AggregationKey;
import com.farkas.familymealmate.model.entity.ShoppingItemEntity;
import com.farkas.familymealmate.util.AggregationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingItemAggregator {

    private ShoppingItemAggregator() {
    }

    public static List<ShoppingItemEntity> aggregate(List<ShoppingItemEntity> items) {
        Map<AggregationKey, ShoppingItemEntity> aggregatedByKey = new HashMap<>();
        List<ShoppingItemEntity> result = new ArrayList<>();

        for (ShoppingItemEntity item : items) {

            if (isFreeTextItem(item)) {
                result.add(item);
                continue;
            }

            AggregationKey key = new AggregationKey(item.getIngredient().getId(), AggregationUtil.autoMatchNull(aggregatedByKey.keySet(), item));
            ShoppingItemEntity existing = aggregatedByKey.get(key);

            if (existing == null) {
                aggregatedByKey.put(key, item);
                result.add(item);
                continue;
            }

            if (AggregationUtil.isAggregatable(item.getQuantity(), item.getMeasurement())) {
                if (AggregationUtil.canConvert(item.getMeasurement(), existing.getMeasurement())) {
                    AggregationUtil.aggregateQuantities(
                            item.getQuantity(),
                            item.getMeasurement(),
                            existing);

                    mergeNotes(item, existing);

                } else {
                    result.add(item);
                }
            }

        }

        return result;
    }

    private static void mergeNotes(ShoppingItemEntity item, ShoppingItemEntity existing) {
        if (item.getNote() != null) {
            if (existing.getNote() == null) {
                existing.setNote(item.getNote());
            } else {
                existing.setNote(existing.getNote() + " | " + item.getNote());
            }
        }
    }

    private static boolean isFreeTextItem(ShoppingItemEntity item) {
        return item.getIngredient() == null;
    }

}

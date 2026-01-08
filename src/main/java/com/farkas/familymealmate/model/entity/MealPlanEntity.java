package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.common.HouseholdOwned;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "meal_plan")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MealPlanEntity extends BaseEntity implements HouseholdOwned {

    @Version
    private Long version;

    @Column(name = "week_start")
    private LocalDate weekStart;

    @Column(name = "is_template", nullable = false)
    private boolean template;

    @Column(name = "template_name", length = 100)
    private String templateName;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private HouseholdEntity household;

    @OneToMany(mappedBy = "mealPlan",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<MealSlotEntity> mealSlots;

    public List<MealSlotEntity> getMealSlots() {
        if (mealSlots == null) {
            mealSlots = new ArrayList<>();
        }
        return mealSlots;
    }
}

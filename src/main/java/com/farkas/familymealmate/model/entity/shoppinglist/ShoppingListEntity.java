package com.farkas.familymealmate.model.entity.shoppinglist;

import com.farkas.familymealmate.model.common.HouseholdOwned;
import com.farkas.familymealmate.model.entity.DirtyAggregateRoot;
import com.farkas.familymealmate.model.entity.household.HouseholdEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "shopping_list")
public class ShoppingListEntity extends DirtyAggregateRoot implements HouseholdOwned {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shopping_list")
    @SequenceGenerator(name = "seq_shopping_list", sequenceName = "seq_shopping_list", allocationSize = 1)
    private Long id;

    @Version
    private Long version;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id", nullable = false)
    private HouseholdEntity household;

    private String note;

    @OneToMany(mappedBy = "shoppingList",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ShoppingItemEntity> shoppingItems;

    public List<ShoppingItemEntity> getShoppingItems() {
        if (shoppingItems == null) {
            shoppingItems = new ArrayList<>();
        }
        return shoppingItems;
    }
}

package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.common.HouseholdOwned;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Recipe")
@Table(name = "recipe")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class RecipeEntity extends BaseEntity implements HouseholdOwned {

    private String title;
    private String description;
    private Integer totalTime;
    private Integer serves;

    @ElementCollection
    @Column(name = "instruction")
    private List<String> instructions;

    @ElementCollection
    @Column(name = "note")
    private List<String> notes;

    @ManyToOne(fetch = FetchType.LAZY)
    private HouseholdEntity household;

    @ManyToOne(fetch = FetchType.LAZY)
    private FamilyMemberEntity createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagEntity> tags;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientEntity> ingredients;

    public List<String> getInstructions() {
        if (instructions == null) {
            instructions = new ArrayList<>();
        }
        return instructions;
    }

    public List<String> getNotes() {
        if (notes == null) {
            instructions = new ArrayList<>();
        }
        return notes;
    }

    public Set<TagEntity> getTags() {
        if (tags == null) {
            tags = new HashSet<>();
        }
        return tags;
    }

    public List<RecipeIngredientEntity> getIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        return ingredients;
    }
}

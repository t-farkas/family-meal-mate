package com.farkas.familymealmate.model.entity.recipe;

import com.farkas.familymealmate.model.common.HouseholdOwned;
import com.farkas.familymealmate.model.entity.BaseEntity;
import com.farkas.familymealmate.model.entity.masterdata.TagEntity;
import com.farkas.familymealmate.model.entity.household.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.household.HouseholdEntity;
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
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class RecipeEntity extends BaseEntity implements HouseholdOwned {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_recipe")
    @SequenceGenerator(name = "seq_recipe", sequenceName = "seq_recipe", allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;
    private String description;
    private Integer totalTime;
    private Integer serves;

    @ElementCollection(fetch =  FetchType.LAZY)
    @Column(name = "instruction")
    @OrderColumn(name = "step_number")
    private List<String> instructions;

    @ElementCollection(fetch =  FetchType.LAZY)
    @Column(name = "note")
    private Set<String> notes;

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

    public Set<String> getNotes() {
        if (notes == null) {
            notes = new HashSet<>();
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

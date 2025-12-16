package com.farkas.familymealmate.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Recipe")
@Table(name = "recipe")
public class RecipeEntity extends BaseEntity {

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

    @ManyToOne
    private HouseholdEntity household;

    @ManyToOne
    private FamilyMemberEntity createdBy;

    @ManyToMany
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagEntity> tags;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredientEntity> ingredients;

}

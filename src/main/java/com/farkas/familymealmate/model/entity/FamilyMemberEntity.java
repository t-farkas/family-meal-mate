package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.AllergyType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "family_member")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class FamilyMemberEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ElementCollection
    @CollectionTable(name = "family_member_allergies", joinColumns = @JoinColumn(name = "family_member_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "allergies")
    private Set<AllergyType> allergies;

    @ManyToOne
    @JoinColumn(name = "household_id", nullable = false)
    private HouseholdEntity household;

    public Set<AllergyType> getAllergies() {
        if (allergies == null) {
            allergies = new HashSet<>();
        }
        return allergies;
    }
}

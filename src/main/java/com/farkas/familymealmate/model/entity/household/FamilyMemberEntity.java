package com.farkas.familymealmate.model.entity.household;

import com.farkas.familymealmate.model.common.HouseholdOwned;
import com.farkas.familymealmate.model.entity.BaseEntity;
import com.farkas.familymealmate.model.enums.AllergyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "family_member")
public class FamilyMemberEntity extends BaseEntity implements HouseholdOwned {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_family_member")
    @SequenceGenerator(name = "seq_family_member", sequenceName = "seq_family_member", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ElementCollection
    @CollectionTable(name = "family_member_allergies", joinColumns = @JoinColumn(name = "family_member_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "allergies")
    private Set<AllergyType> allergies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id", nullable = false)
    private HouseholdEntity household;

    public Set<AllergyType> getAllergies() {
        if (allergies == null) {
            allergies = new HashSet<>();
        }
        return allergies;
    }
}

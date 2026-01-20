package com.farkas.familymealmate.model.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "household")
public class HouseholdEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_household")
    @SequenceGenerator(name = "seq_household", sequenceName = "seq_household", allocationSize = 1)
    private Long id;

    String joinId;
    String name;

    @OneToMany(mappedBy = "household", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FamilyMemberEntity> members;

    public Set<FamilyMemberEntity> getMembers() {
        if (members == null) {
            members = new HashSet<>();
        }
        return members;
    }
}

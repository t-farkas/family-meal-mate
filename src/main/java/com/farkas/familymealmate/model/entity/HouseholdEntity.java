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

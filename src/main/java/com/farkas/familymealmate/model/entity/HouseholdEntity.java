package com.farkas.familymealmate.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.CascadeType;

import java.util.Set;

@Data
@ToString(exclude = "members")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "household")
public class HouseholdEntity extends BaseEntity {

    String joinId;
    String name;

    @OneToMany(mappedBy = "household", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FamilyMemberEntity> members;
}

package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.AllergyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "family_member")
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

}

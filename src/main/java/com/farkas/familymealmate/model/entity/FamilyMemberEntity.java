package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.AllergyType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

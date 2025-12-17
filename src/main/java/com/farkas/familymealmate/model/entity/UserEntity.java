package com.farkas.familymealmate.model.entity;

import com.farkas.familymealmate.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "family_member_id", nullable = false)
    private FamilyMemberEntity familyMember;

}

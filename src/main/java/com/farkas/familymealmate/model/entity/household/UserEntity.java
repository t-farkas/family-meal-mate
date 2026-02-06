package com.farkas.familymealmate.model.entity.household;

import com.farkas.familymealmate.model.entity.BaseEntity;
import com.farkas.familymealmate.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "family_member_id", nullable = false)
    private FamilyMemberEntity familyMember;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity user)) return false;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}

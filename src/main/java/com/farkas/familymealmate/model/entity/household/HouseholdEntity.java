package com.farkas.familymealmate.model.entity.household;

import com.farkas.familymealmate.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HouseholdEntity that)) return false;
        return Objects.equals(joinId, that.joinId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(joinId);
    }
}

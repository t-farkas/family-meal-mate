package com.farkas.familymealmate.model.entity.masterdata;

import com.farkas.familymealmate.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tag")
public class TagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tag")
    @SequenceGenerator(name = "seq_tag", sequenceName = "seq_tag", allocationSize = 100)
    private Long id;

    private String name;

    public TagEntity(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity tagEntity)) return false;
        return Objects.equals(name, tagEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}

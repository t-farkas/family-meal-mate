package com.farkas.familymealmate.model.entity.masterdata;

import com.farkas.familymealmate.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tag")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class TagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tag")
    @SequenceGenerator(name = "seq_tag", sequenceName = "seq_tag", allocationSize = 100)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    public TagEntity(String name) {
        this.name = name;
    }
}

package com.farkas.familymealmate.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tag")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TagEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tag")
    @SequenceGenerator(name = "seq_tag", sequenceName = "seq_tag", allocationSize = 100)
    private Long id;

    private String name;

    public TagEntity(String name) {
        this.name = name;
    }
}

package com.farkas.familymealmate.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor @NoArgsConstructor
@Table(name = "tag")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class TagEntity extends BaseEntity {

    private String name;
}

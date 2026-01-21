package com.farkas.familymealmate.model.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class DirtyAggregateRoot extends BaseEntity {

    public void markDirty() {
        this.setUpdatedAt(LocalDateTime.now());
    }
}

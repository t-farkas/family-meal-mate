package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingListEntity, Long> {

    Optional<ShoppingListEntity> findByHouseholdId(Long householdId);

}

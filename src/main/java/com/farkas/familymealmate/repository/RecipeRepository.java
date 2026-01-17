package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {

    @Query("""
                SELECT r
                FROM Recipe r
                LEFT JOIN FETCH r.ingredients ri
                LEFT JOIN FETCH ri.ingredient i
                LEFT JOIN FETCH i.allergies
                LEFT JOIN FETCH r.tags
                WHERE r.id = :id
            """)
    Optional<RecipeEntity> getFullEntityById(Long id);

}

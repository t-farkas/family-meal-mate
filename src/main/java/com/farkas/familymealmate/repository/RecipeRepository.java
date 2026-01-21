package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.RecipeEntity;
import com.farkas.familymealmate.model.entity.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long>, JpaSpecificationExecutor<RecipeEntity> {


    @EntityGraph(attributePaths = {"ingredients", "ingredients.ingredient", "createdBy"})
    Optional<RecipeEntity> findWithIngredientsById(@Param("id") Long id);

    @Query("SELECT n FROM Recipe r JOIN r.notes n WHERE r.id= :recipeId")
    Set<String> findNotesByRecipeId(@Param("recipeId") Long recipeID);

    @Query("SELECT i FROM Recipe r JOIN r.instructions i WHERE r.id = :recipeId ORDER BY INDEX(i) ")
    List<String> findInstructionsByRecipeId(@Param("recipeId") Long recipeId);

    @Query("SELECT t FROM Recipe r JOIN r.tags t WHERE r.id = :recipeId")
    Set<TagEntity> findTagsByRecipeId(@Param("recipeId") Long recipeId);

    @EntityGraph(attributePaths = "createdBy")
    @NonNull
    Page<RecipeEntity> findAll(@NonNull Specification<RecipeEntity> spec, @NonNull Pageable pageable);

    List<RecipeEntity> findAllByIdInAndHouseholdId(Set<Long> ids, Long householdId);

}

package com.farkas.familymealmate.repository;

import com.farkas.familymealmate.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN FETCH u.familyMember fm " +
            "JOIN FETCH fm.household h " +
            "WHERE u.email = :email")
    Optional<UserEntity> getFullEntityByEmail(String email);
}

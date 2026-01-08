package com.farkas.familymealmate.service.mealplan;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.MealPlanEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.repository.MealPlanRepository;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;

@SpringBootTest
public class MealPlanOptimisticLockingTest {

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private MealPlanRepository repository;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
        userFactory.deleteAll();
    }

    @Test
    void throwsOptimisticLockingException() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        HouseholdEntity household = user.getFamilyMember().getHousehold();

        MealPlanEntity entity = new MealPlanEntity();
        entity.setWeekStart(LocalDate.now());
        entity.setHousehold(household);
        entity.setVersion(0L);

        entity = repository.saveAndFlush(entity);

        MealPlanEntity user1 = repository.findById(entity.getId()).get();
        MealPlanEntity user2 = repository.findById(entity.getId()).get();

        user1.setWeekStart(LocalDate.EPOCH);
        user2.setWeekStart(LocalDate.MAX);

        repository.saveAndFlush(user1);

        Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.saveAndFlush(user2));

        MealPlanEntity saved = repository.findById(entity.getId()).get();
        Assertions.assertEquals(1L, saved.getVersion());
        Assertions.assertEquals(LocalDate.EPOCH, saved.getWeekStart());
    }

}

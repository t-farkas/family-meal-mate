package com.farkas.familymealmate.service.shoppinglist;

import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.ShoppingListEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@SpringBootTest
public class ShoppingListOptimisticLockingTest {

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private ShoppingListRepository repository;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
        userFactory.deleteAll();
    }

    @Test
    void throwsOptimisticLockingException() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        HouseholdEntity household = user.getFamilyMember().getHousehold();

        ShoppingListEntity user1 = repository.findByHouseholdId(household.getId()).get();
        ShoppingListEntity user2 = repository.findByHouseholdId(household.getId()).get();

        user1.setNote("Note 1");
        user2.setNote("Note 2");

        repository.saveAndFlush(user1);

        Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.saveAndFlush(user2));

        ShoppingListEntity saved = repository.findById(household.getId()).get();
        Assertions.assertEquals(1L, saved.getVersion());
        Assertions.assertEquals("Note 1", saved.getNote());
    }

}

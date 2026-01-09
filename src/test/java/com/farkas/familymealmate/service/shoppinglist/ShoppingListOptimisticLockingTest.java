package com.farkas.familymealmate.service.shoppinglist;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.shoppinglist.ShoppingListUpdateRequest;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.service.ShoppingListService;
import com.farkas.familymealmate.testdata.recipe.TestRecipes;
import com.farkas.familymealmate.testdata.shoppingList.TestShoppingListBuilder;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class ShoppingListOptimisticLockingTest {

    private final TestShoppingListBuilder shoppingListBuilder = new TestShoppingListBuilder();

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private ShoppingListService service;


    @Test
    void throwsOptimisticLockingException() {

        setupUser();

        ShoppingListUpdateRequest version0 = shoppingListBuilder
                .addRecipeIngredients(TestRecipes.VEGGIE_BOWL)
                .version(0L)
                .note("Version 0")
                .buildUpdateRequest();

        service.update(version0);

        ShoppingListUpdateRequest version1 = shoppingListBuilder
                .addRecipeIngredients(TestRecipes.VEGGIE_BOWL)
                .version(0L)
                .note("Version 1")
                .buildUpdateRequest();

        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> service.update(version1))
                .satisfies(e -> assertThat(e.getErrorCode()).isEqualTo(ErrorCode.SHOPPING_LIST_VERSION_MISMATCH));
    }

    private void setupUser() {
        UserEntity user = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user);
    }

}

package com.farkas.familymealmate.testdata.user;

import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.repository.HouseholdRepository;
import com.farkas.familymealmate.repository.ShoppingListRepository;
import com.farkas.familymealmate.repository.UserRepository;
import com.farkas.familymealmate.security.AuthService;
import com.farkas.familymealmate.security.CustomUserDetails;
import com.farkas.familymealmate.testutil.AuthenticationUtil;
import org.springframework.stereotype.Component;

@Component
public class TestUserFactory {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final HouseholdRepository householdRepository;
    private final ShoppingListRepository shoppingListRepository;

    public TestUserFactory(AuthService authService, UserRepository userRepository, HouseholdRepository householdRepository, ShoppingListRepository shoppingListRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.householdRepository = householdRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    public UserEntity registerWithNewHousehold(TestUser user){
        RegisterRequest registerRequest = user.registerNewHousehold();
        authService.register(registerRequest);
        return userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
    }

    public UserEntity registerWithJoinId(TestUser user, String joinId){
        RegisterRequest registerRequest = user.registerExistingHousehold(joinId);
        authService.register(registerRequest);
        return userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
    }

    public void authenticate(UserEntity user){
        AuthenticationUtil.authenticateAs(new CustomUserDetails(user));
    }


    public void deleteAll() {
        shoppingListRepository.deleteAll();
        userRepository.deleteAll();
        householdRepository.deleteAll();
    }
}

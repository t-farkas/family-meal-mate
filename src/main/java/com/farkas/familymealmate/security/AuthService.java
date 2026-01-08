package com.farkas.familymealmate.security;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.auth.LoginRequest;
import com.farkas.familymealmate.model.dto.auth.LoginResponse;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.model.enums.Role;
import com.farkas.familymealmate.repository.UserRepository;
import com.farkas.familymealmate.security.jwt.JwtService;
import com.farkas.familymealmate.service.FamilyMemberService;
import com.farkas.familymealmate.service.HouseholdService;
import com.farkas.familymealmate.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final FamilyMemberService familyMemberService;
    private final HouseholdService householdService;
    private final ShoppingListService shoppingListService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailService;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {

        HouseholdEntity houseHold;

        if (noHouseholdDetailsProvied(request)) {
            throw new ServiceException(ErrorCode.MISSING_HOUSEHOLD_DETAILS);
        } else if (shouldJoinHousehold(request)) {
            houseHold = householdService.findByJoinId(request.getHouseholdJoinId());
        } else {
            houseHold = householdService.createHouseHold(request.getHouseholdName());
            shoppingListService.create(houseHold);
        }

        FamilyMemberEntity familyMemberEntity = familyMemberService.createFamilyMember(request.getFamilyMemberCreateRequest(), houseHold);
        UserEntity userEntity = getUserEntity(request, familyMemberEntity);
        userRepository.save(userEntity);
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = userDetailService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails.getUsername());

        return new LoginResponse(token);
    }

    private boolean noHouseholdDetailsProvied(RegisterRequest request) {
        return ((request.getHouseholdJoinId() == null || request.getHouseholdJoinId().isBlank())
                && (request.getHouseholdName() == null || request.getHouseholdName().isBlank()));
    }

    private boolean shouldJoinHousehold(RegisterRequest request) {
        String joinId = request.getHouseholdJoinId();
        return joinId != null && !joinId.isBlank();
    }

    private UserEntity getUserEntity(RegisterRequest request, FamilyMemberEntity familyMemberEntity) {
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setFamilyMember(familyMemberEntity);
        return user;
    }

}

package com.farkas.familymealmate.security;

import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrentUserService {

    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userDetails.getUsername()));
    }

    public FamilyMemberEntity getCurrentFamilyMember() {
        return getCurrentUser().getFamilyMember();
    }

    public HouseholdEntity getCurrentHousehold() {
        return getCurrentFamilyMember().getHousehold();
    }
}

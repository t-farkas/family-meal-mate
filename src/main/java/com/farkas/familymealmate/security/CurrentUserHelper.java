package com.farkas.familymealmate.security;

import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserHelper {

    private CurrentUserHelper() {
    }

    public static UserEntity getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }

    public static FamilyMemberEntity getCurrentFamilyMember() {
        return getCurrentUser().getFamilyMember();
    }

    public static HouseholdEntity getCurrentHousehold() {
        return getCurrentFamilyMember().getHousehold();
    }
}

package com.farkas.familymealmate.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationUtil {

    public static void authenticateAs(UserDetails userDetails){
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}

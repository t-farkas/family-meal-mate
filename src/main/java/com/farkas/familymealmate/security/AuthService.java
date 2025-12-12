package com.farkas.familymealmate.security;

import com.farkas.familymealmate.model.dto.auth.LoginRequest;
import com.farkas.familymealmate.model.dto.auth.LoginResponse;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.Role;
import com.farkas.familymealmate.repository.UserRepository;
import com.farkas.familymealmate.security.jwt.JwtService;
import com.farkas.familymealmate.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final FamilyMemberService familyMemberService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService userDetailService;
    private final JwtService jwtService;


    public void register(RegisterRequest request) {

        FamilyMemberEntity familyMemberEntity = familyMemberService.createFamilyMember(request.getFamilyMemberCreateRequest());
        UserEntity userEntity = getUserEntity(request, familyMemberEntity);
        userRepository.save(userEntity);
    }

    private UserEntity getUserEntity(RegisterRequest request, FamilyMemberEntity familyMemberEntity) {
        return UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .familyMember(familyMemberEntity)
                .build();
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = userDetailService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(userDetails.getUsername());

        return new LoginResponse(token);
    }
}

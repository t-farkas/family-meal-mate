package com.farkas.familymealmate.service;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDetailsDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.UserRepository;
import com.farkas.familymealmate.security.AuthService;
import com.farkas.familymealmate.security.CustomUserDetails;
import com.farkas.familymealmate.testdata.familymember.TestFamilyMembers;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.util.AuthenticationUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@SpringBootTest
@Transactional
public class FamilyMemberServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clearSecurityContext() {
        AuthenticationUtil.clear();
    }

    @Test
    void userCannotAccessOtherHouseholdMembersDetails() {

        RegisterRequest request1 = TestUsers.TIM.registerNewHousehold("Tim's household");
        authService.register(request1);
        UserEntity user1 = userRepository.findByEmail(request1.getEmail()).orElseThrow();

        RegisterRequest request2 = TestUsers.JOHN.registerNewHousehold("John's household");
        authService.register(request2);
        UserEntity user2 = userRepository.findByEmail(request2.getEmail()).orElseThrow();

        AuthenticationUtil.authenticateAs(new CustomUserDetails(user1));

        assertThatExceptionOfType(ServiceException.class).isThrownBy(() ->
                        familyMemberService.getFamilyMember(user2.getFamilyMember().getId()))
                .satisfies(ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORIZATION));

    }

    @Test
    void userCanAccessOwnHouseholdMembersDetails() {

        RegisterRequest request1 = TestUsers.TIM.registerNewHousehold("Tim's household");
        authService.register(request1);
        UserEntity user1 = userRepository.findByEmail(request1.getEmail()).orElseThrow();
        String joinId = user1.getFamilyMember().getHousehold().getJoinId();

        RegisterRequest request2 = TestUsers.JOHN.registerExistingHousehold(joinId);
        authService.register(request2);
        UserEntity user2 = userRepository.findByEmail(request2.getEmail()).orElseThrow();

        AuthenticationUtil.authenticateAs(new CustomUserDetails(user1));

        FamilyMemberDetailsDto familyMember = familyMemberService.getFamilyMember(user2.getFamilyMember().getId());
        assertThat(familyMember).isNotNull();
        assertThat(familyMember.getName()).isEqualTo(request2.getFamilyMemberCreateRequest().getName());

    }

    @Test
    void addFamilyMemberUsesCurrentHousehold() {

        RegisterRequest request1 = TestUsers.BERTHA.registerNewHousehold("Bertha's household");
        authService.register(request1);
        UserEntity user1 = userRepository.findByEmail(request1.getEmail()).orElseThrow();

        AuthenticationUtil.authenticateAs(new CustomUserDetails(user1));
        FamilyMemberDetailsDto member = familyMemberService.addFamilyMember(TestFamilyMembers.JANE.createRequest());

        assertThat(member).isNotNull();
        assertThat(member.getHousehold().getId()).isEqualTo(user1.getFamilyMember().getHousehold().getId());

    }

}

package com.farkas.familymealmate.security;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.auth.RegisterRequest;
import com.farkas.familymealmate.model.entity.FamilyMemberEntity;
import com.farkas.familymealmate.model.entity.HouseholdEntity;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.repository.FamilyMemberRepository;
import com.farkas.familymealmate.repository.HouseholdRepository;
import com.farkas.familymealmate.repository.UserRepository;
import com.farkas.familymealmate.testdata.user.TestUsers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private HouseholdRepository householdRepository;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerCreatesNewHouseholdWithFamilyMember() {
        RegisterRequest registerRequest = TestUsers.TIM.registerNewHousehold();
        authService.register(registerRequest);

        List<HouseholdEntity> households = householdRepository.findAll();
        assertThat(households.size()).isEqualTo(1);

        HouseholdEntity household = households.get(0);
        assertThat(household.getName()).isEqualTo(registerRequest.getHouseholdName());
        assertThat(household.getJoinId()).isNotBlank();

        List<FamilyMemberEntity> members = familyMemberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);

        FamilyMemberEntity member = members.get(0);
        assertThat(member.getHousehold().getId()).isEqualTo(household.getId());

        UserEntity user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
        assertThat(user.getFamilyMember().getId()).isEqualTo(member.getId());

    }

    @Test
    void registerJoinsHousehold() {
        RegisterRequest registerRequest = TestUsers.TIM.registerNewHousehold();
        authService.register(registerRequest);
        List<HouseholdEntity> householdsFirstReg = householdRepository.findAll();
        assertThat(householdsFirstReg.size()).isEqualTo(1);
        HouseholdEntity household = householdsFirstReg.get(0);

        RegisterRequest registerRequestJoinId = TestUsers.BERTHA.registerExistingHousehold(household.getJoinId());
        authService.register(registerRequestJoinId);

        List<HouseholdEntity> householdsSecondReg = householdRepository.findAll();
        assertThat(householdsSecondReg.size()).isEqualTo(householdsFirstReg.size());

        List<FamilyMemberEntity> members = familyMemberRepository.findAll();
        assertThat(members).allMatch(member -> member.getHousehold().getId().equals(household.getId()));

    }

    @Test
    void registerIncorrectJoinId() {
        RegisterRequest registerRequest = TestUsers.BERTHA.registerNewHousehold();
        authService.register(registerRequest);

        RegisterRequest registerRequestJoinId = TestUsers.JOHN.registerExistingHousehold("ABCD");
        assertThatExceptionOfType(ServiceException.class)
                .isThrownBy(() -> authService.register(registerRequestJoinId))
                .satisfies(ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.HOUSEHOLD_NOT_FOUND));

    }
}

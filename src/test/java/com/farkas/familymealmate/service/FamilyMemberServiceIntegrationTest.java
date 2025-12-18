package com.farkas.familymealmate.service;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDetailsDto;
import com.farkas.familymealmate.model.entity.UserEntity;
import com.farkas.familymealmate.model.enums.ErrorCode;
import com.farkas.familymealmate.testdata.familymember.TestFamilyMembers;
import com.farkas.familymealmate.testdata.user.TestUserFactory;
import com.farkas.familymealmate.testdata.user.TestUsers;
import com.farkas.familymealmate.testutil.AuthenticationUtil;
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
    private FamilyMemberService familyMemberService;

    @Autowired
    private TestUserFactory userFactory;

    @AfterEach
    void clearSecurityContext() {
        AuthenticationUtil.clear();
    }

    @Test
    void userCannotAccessOtherHouseholdMembersDetails() {

        UserEntity user1 = userFactory.registerWithNewHousehold(TestUsers.TIM);
        UserEntity user2 = userFactory.registerWithNewHousehold(TestUsers.JOHN);

        userFactory.authenticate(user1);

        assertThatExceptionOfType(ServiceException.class).isThrownBy(() ->
                        familyMemberService.getFamilyMember(user2.getFamilyMember().getId()))
                .satisfies(ex -> assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NO_AUTHORIZATION));

    }

    @Test
    void userCanAccessOwnHouseholdMembersDetails() {

        UserEntity user1 = userFactory.registerWithNewHousehold(TestUsers.TIM);
        String joinId = user1.getFamilyMember().getHousehold().getJoinId();

        UserEntity user2 = userFactory.registerWithJoinId(TestUsers.JOHN, joinId);
        userFactory.authenticate(user1);

        FamilyMemberDetailsDto familyMember = familyMemberService.getFamilyMember(user2.getFamilyMember().getId());
        assertThat(familyMember).isNotNull();
        assertThat(familyMember.getName()).isEqualTo(TestUsers.JOHN.name());

    }

    @Test
    void addFamilyMemberUsesCurrentHousehold() {

        UserEntity user1 = userFactory.registerWithNewHousehold(TestUsers.BERTHA);
        userFactory.authenticate(user1);

        FamilyMemberDetailsDto member = familyMemberService.addFamilyMember(TestFamilyMembers.JANE.createRequest());

        assertThat(member).isNotNull();
        assertThat(member.getHousehold().getName()).isEqualTo(user1.getFamilyMember().getHousehold().getName());

    }

}

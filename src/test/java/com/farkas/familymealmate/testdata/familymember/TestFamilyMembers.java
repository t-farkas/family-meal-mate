package com.farkas.familymealmate.testdata.familymember;

import com.farkas.familymealmate.model.enums.AllergyType;

import java.time.LocalDate;
import java.util.Set;

public class TestFamilyMembers {

    public static final TestFamilyMember JANE = new TestFamilyMember(
            "Jane",
            LocalDate.of(1977, 6, 18),
            Set.of(AllergyType.DAIRY, AllergyType.SESAME));
}

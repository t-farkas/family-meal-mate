package com.farkas.familymealmate.testdata.user;

import java.time.LocalDate;

public class TestUsers {

    public static final TestUser TIM = new TestUser(
            "tim@example.com",
            "secret",
            "Tim",
            LocalDate.of(1990, 5, 5)
    );

    public static final TestUser BERTHA = new TestUser(
            "bertha@example.com",
            "hush-hush",
            "Bertha",
            LocalDate.of(1994, 10, 15)
    );

    public static final TestUser JOHN = new TestUser(
            "john@example.com",
            "password",
            "John",
            LocalDate.of(1989, 4, 4)
    );


}

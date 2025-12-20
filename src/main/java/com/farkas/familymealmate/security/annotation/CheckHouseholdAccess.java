package com.farkas.familymealmate.security.annotation;

import com.farkas.familymealmate.model.enums.HouseholdOwnedResourceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckHouseholdAccess {

    String idArg() default "id";
    HouseholdOwnedResourceType type();
}

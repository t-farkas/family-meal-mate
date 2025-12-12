package com.farkas.familymealmate.model.dto.auth;

import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @Email(message = "{auth.email.invalid}")
    @NotBlank(message = "{auth.email.notblank}")
    private String email;

    @NotBlank(message = "{auth.password.notblank}")
    private String password;

    @NotNull(message = "{auth.familymember.notnull}")
    @Valid
    private FamilyMemberCreateRequest familyMemberCreateRequest;

}

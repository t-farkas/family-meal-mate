package com.farkas.familymealmate.model.dto.familymember;

import com.farkas.familymealmate.model.enums.AllergyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMemberCreateRequest {

    @NotBlank(message = "{familymember.name.notblank}")
    private String name;

    @NotNull(message = "{familymember.dateofbirth.notnull}")
    @Past(message = "{familymember.dateofbirth.past}")
    private LocalDate dateOfBirth;

    private Set<AllergyType> allergies;

}

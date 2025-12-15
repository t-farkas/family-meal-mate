package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDetailsDto;
import com.farkas.familymealmate.service.FamilyMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familymember")
@RequiredArgsConstructor
public class FamilyMemberController {

    private final FamilyMemberService service;

    @PostMapping
    public ResponseEntity<BaseResponse> addFamilyMember(@RequestBody @Valid FamilyMemberCreateRequest request) {

        service.addFamilyMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Successfully added family member"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyMemberDetailsDto> getFamilyMember(@PathVariable Long id) {

        FamilyMemberDetailsDto familyMember = service.getFamilyMember(id);
        return ResponseEntity.ok(familyMember);
    }
}

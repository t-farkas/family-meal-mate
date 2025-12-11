package com.farkas.familymealmate.controller;

import com.farkas.familymealmate.exception.ServiceException;
import com.farkas.familymealmate.model.dto.BaseResponse;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberCreateRequest;
import com.farkas.familymealmate.model.dto.familymember.FamilyMemberDto;
import com.farkas.familymealmate.service.FamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/familymember")
@RequiredArgsConstructor
public class FamilyMemberController {

    private final FamilyMemberService service;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createFamilyMember(@RequestBody FamilyMemberCreateRequest request) throws ServiceException {

        service.createFamilyMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponse("Successfully created family member"));
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<FamilyMemberDto> getFamilyMember(@PathVariable Long id) throws ServiceException {

        FamilyMemberDto familyMember = service.getFamilyMember(id);
        return ResponseEntity.ok(familyMember);
    }
}

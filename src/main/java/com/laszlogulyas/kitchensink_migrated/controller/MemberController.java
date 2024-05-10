package com.laszlogulyas.kitchensink_migrated.controller;

import com.laszlogulyas.kitchensink_migrated.data.MemberRepository;
import com.laszlogulyas.kitchensink_migrated.model.Member;
import com.laszlogulyas.kitchensink_migrated.service.MemberRegistration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/kitchensink/rest/members")
@Validated
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final Validator validator;
    private final MemberRepository repository;
    private final MemberRegistration registration;

    @GetMapping
    @Operation(summary = "List all members", description = "Retrieve a list of all members sorted by name in ascending order")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    public List<Member> listAllMembers() {
        return repository.findAllByOrderByNameAsc();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lookup member by ID", description = "Find a member by their ID")
    @ApiResponse(responseCode = "200", description = "Member found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Member.class)))
    @ApiResponse(responseCode = "404", description = "Member not found")
    public Member lookupMemberById(@PathVariable("id") long id) {
        Optional<Member> member = repository.findById(id);
        if (member.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
        return member.get();
    }

    @PostMapping
    @Operation(summary = "Create a new member", description = "Register a new member with all mandatory fields")
    @ApiResponse(responseCode = "200", description = "Member registered successfully")
    @ApiResponse(responseCode = "400", description = "Bad request due to input validation failure")
    @ApiResponse(responseCode = "409", description = "Conflict - Email already exists")
    public ResponseEntity<Map<String, String>> createMember(@RequestBody Member member) {
        try {
            validateMember(member);
            registration.register(member);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException ce) {
            return createViolationResponse(ce.getConstraintViolations());
        } catch (ValidationException e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "Email taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseObj);
        } catch (Exception e) {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObj);
        }
    }

    private void validateMember(Member member) throws ValidationException {
        Set<ConstraintViolation<Member>> violations = validator.validate(member);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private ResponseEntity<Map<String, String>> createViolationResponse(Set<ConstraintViolation<?>> violations) {
        log.info("Validation completed. violations found: {}", violations.size());
        Map<String, String> responseObj = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.badRequest().body(responseObj);
    }

    public boolean emailAlreadyExists(String email) {
        Member member = repository.findByEmail(email);
        return member != null;
    }
}

package com.laszlogulyas.kitchensink_migrated.controller;

import com.laszlogulyas.kitchensink_migrated.AbstractUnitTest;
import com.laszlogulyas.kitchensink_migrated.model.Member;
import com.laszlogulyas.kitchensink_migrated.service.MemberService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberControllerTest extends AbstractUnitTest {

    @Mock
    private Validator validator;

    @Mock
    private MemberService memberService;

    @Mock
    private ConstraintViolation constraintViolation;

    @InjectMocks
    private MemberController memberController;

    @Test
    void testListAllMembers() {
        List<Member> members = new ArrayList<>();
        members.add(TEST_MEMBER_1);
        members.add(TEST_MEMBER_2);
        when(memberService.getAllMembers()).thenReturn(members);

        List<Member> result = memberController.listAllMembers();
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertEquals(TEST_MEMBER_1.getName(), result.get(0).getName()),
                () -> assertEquals(TEST_MEMBER_2.getName(), result.get(1).getName())
        );
    }

    @Test
    void testLookupMemberById_memberFound() {
        when(memberService.getMemberById(TEST_MEMBER_1.getId())).thenReturn(Optional.of(TEST_MEMBER_1));

        Member result = memberController.lookupMemberById(TEST_MEMBER_1.getId());
        assertEquals(TEST_MEMBER_1.getName(), result.getName());
    }

    @Test
    void testLookupMemberById_memberNotFound() {
        when(memberService.getMemberById(TEST_MEMBER_1.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> memberController.lookupMemberById(TEST_MEMBER_1.getId()));
    }

    @Test
    void testCreateMember_success() throws Exception {
        ResponseEntity<Map<String, String>> responseEntity = memberController.createMember(TEST_MEMBER_1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(memberService, times(1)).register(TEST_MEMBER_1);
    }

    @Test
    void testCreateMember_badRequestValidationFailure() {
        Set<ConstraintViolation<Member>> violations = Set.of(constraintViolation);
        when(validator.validate(TEST_MEMBER_1)).thenReturn(violations);
        when(constraintViolation.getPropertyPath()).thenReturn(PathImpl.createPathFromString("testpath"));

        ResponseEntity<Map<String, String>> responseEntity = memberController.createMember(TEST_MEMBER_1);
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertFalse(responseEntity.getBody().isEmpty())
        );
    }

    @Test
    void testCreateMember_conflictEmailAlreadyExists() {
        when(memberService.checkEmailAlreadyExists(TEST_MEMBER_1)).thenReturn(true);

        ResponseEntity<Map<String, String>> responseEntity = memberController.createMember(TEST_MEMBER_1);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode()),
                () -> assertEquals("Email taken", responseEntity.getBody().get("email"))
        );
    }

    @Test
    void testCreateMember_serverError() throws Exception {
        doThrow(new RuntimeException("Server error")).when(memberService).register(TEST_MEMBER_1);

        ResponseEntity<Map<String, String>> responseEntity = memberController.createMember(TEST_MEMBER_1);
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertEquals("Server error", responseEntity.getBody().get("error"))
        );
    }
}

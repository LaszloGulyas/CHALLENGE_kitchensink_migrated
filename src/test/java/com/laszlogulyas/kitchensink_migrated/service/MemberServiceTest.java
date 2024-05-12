package com.laszlogulyas.kitchensink_migrated.service;

import com.laszlogulyas.kitchensink_migrated.AbstractUnitTest;
import com.laszlogulyas.kitchensink_migrated.data.MemberRepository;
import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberServiceTest extends AbstractUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void testRegister() throws Exception {
        memberService.register(TEST_MEMBER_1);
        verify(memberRepository, times(1)).insert(TEST_MEMBER_1);
    }

    @Test
    void testGetAllMembers() {
        when(memberRepository.findAllByOrderByNameAsc()).thenReturn(List.of(
                TEST_MEMBER_1,
                TEST_MEMBER_2
        ));
        List<Member> members = memberService.getAllMembers();
        assertAll(
                () -> assertEquals(2, members.size()),
                () -> assertEquals(TEST_MEMBER_1.getName(), members.get(0).getName()),
                () -> assertEquals(TEST_MEMBER_2.getName(), members.get(1).getName())
        );
    }

    @Test
    void testGetMemberById() {
        when(memberRepository.findById(new ObjectId(TEST_MEMBER_1.getId()))).thenReturn(Optional.of(TEST_MEMBER_1));
        Optional<Member> optionalMember = memberService.getMemberById(TEST_MEMBER_1.getId());
        assertAll(
                () -> assertTrue(optionalMember.isPresent()),
                () -> assertEquals(TEST_MEMBER_1.getName(), optionalMember.get().getName())
        );
    }

    @Test
    void testCheckEmailAlreadyExists() {
        when(memberRepository.findByEmail(TEST_MEMBER_1.getEmail())).thenReturn(TEST_MEMBER_1);
        assertTrue(memberService.checkEmailAlreadyExists(TEST_MEMBER_1));
    }
}

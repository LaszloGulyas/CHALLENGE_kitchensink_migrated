package com.laszlogulyas.kitchensink_migrated.service;

import com.laszlogulyas.kitchensink_migrated.data.MemberRepository;
import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void register(Member member) throws Exception {
        log.info("Registering {}", member.getName());
        memberRepository.insert(member);
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMemberById(String id) {
        return memberRepository.findById(new ObjectId(id));
    }

    @Transactional(readOnly = true)
    public boolean checkEmailAlreadyExists(Member member) {
        return memberRepository.findByEmail(member.getEmail()) != null;
    }
}

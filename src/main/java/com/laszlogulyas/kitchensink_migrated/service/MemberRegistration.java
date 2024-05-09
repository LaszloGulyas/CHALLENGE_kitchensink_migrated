package com.laszlogulyas.kitchensink_migrated.service;

import com.laszlogulyas.kitchensink_migrated.data.MemberRepository;
import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRegistration {

    private final MemberRepository memberRepository;

    @Transactional
    public void register(Member member) throws Exception {
        log.info("Registering {}", member.getName());
        memberRepository.save(member);
    }
}

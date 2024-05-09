package com.laszlogulyas.kitchensink_migrated.data;

import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    List<Member> findAllByOrderByNameAsc();
}

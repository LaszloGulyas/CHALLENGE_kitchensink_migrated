package com.laszlogulyas.kitchensink_migrated.data;

import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends MongoRepository<Member, ObjectId> {

    Member findByEmail(String email);

    List<Member> findAllByOrderByNameAsc();
}

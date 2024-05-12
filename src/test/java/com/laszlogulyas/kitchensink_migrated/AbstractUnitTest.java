package com.laszlogulyas.kitchensink_migrated;

import com.laszlogulyas.kitchensink_migrated.model.Member;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
public class AbstractUnitTest {

    protected static final Member TEST_MEMBER_1 = new Member("664070459f54f636534bc94a", "John Doe 1", "john1@example.com", "1234567891");
    protected static final Member TEST_MEMBER_2 = new Member("6640741e9f54f636534bc94b", "John Doe 2", "john2@example.com", "1234567892");
}

package com.desiredchargingstation.map.repository;

import com.desiredchargingstation.map.model.AuthProvider;
import com.desiredchargingstation.map.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenEmail_whenFindByEmail_thenReturnUser() {

        // given
        User john = new User();
        john.setName("John");
        john.setEmail("john@gmail.com");
        john.setProvider(AuthProvider.google);
        john.setChargingPoints(new ArrayList<>());
        testEntityManager.persist(john);
        testEntityManager.flush();

        // when
        Optional<User> foundOptional = userRepository.findUserByEmail(john.getEmail());
        User found = foundOptional.orElse(null);

        // then
        assertThat(found.getName(), equalTo(john.getName()));
    }
}

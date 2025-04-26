package kz.tottory.app.user.impl;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepo {

    private static final User user1 = new User(1L, "john.doe@mail.com", "John Doe", true);

    List<User> findAll() {
        return List.of(user1);
    }

    Optional<User> findById(Long id) {
        return Optional.of(user1);
    }
}

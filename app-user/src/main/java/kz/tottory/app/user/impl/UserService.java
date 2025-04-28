package kz.tottory.app.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepo repo;

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findById(Long id) {
        var user = repo.findById(id).get();
        log.info("sending user {}", user);
        return user;
    }

    public User create(User user) {
        log.info(user.toString());
        return user;
    }
}

package kz.tottory.app.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
//        return service.create(user);
//        throw new RuntimeException("some error");
        throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "hello");
    }

}

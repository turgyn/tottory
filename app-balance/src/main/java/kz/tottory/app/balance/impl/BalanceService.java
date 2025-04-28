package kz.tottory.app.balance.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {

    public final UserClient userClient;

    public Mono<Balance> getBalance(Long userId) {
        return userClient.getUser(userId)
                .map(user -> {
                    log.info("we got everything {}", user);
                    return new Balance(userId, new BigDecimal("1000.01"));
                });
    }
}

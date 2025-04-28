package kz.tottory.app.balance.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{userId}")
    public Mono<Balance> getBalance(@PathVariable Long userId) {
        return balanceService.getBalance(userId);
    }

    @PostMapping
    public Mono<Balance> patchBalance(@RequestBody Balance balance) {
        return Mono.just(balance);
    }
}

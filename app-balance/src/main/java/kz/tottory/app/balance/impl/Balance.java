package kz.tottory.app.balance.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    private Long userId;
    private BigDecimal amount;
}

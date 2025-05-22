package com.bookcatalog.strategy;

import java.math.BigDecimal;

/**
 * 电子书逾期费用策略实现（策略模式）
 */
public class EBookOverdueFeeStrategy implements OverdueFeeStrategy {
    private static final BigDecimal FEE_PER_DAY = new BigDecimal("0.2"); // 每天0.2元

    @Override
    public BigDecimal calculateOverdueFee(long overdueDays) {
        return FEE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
    }
}    
package com.bookcatalog.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 纸质书逾期费用策略实现（策略模式）
 */
@Component("wechatpay")
public class PaperBookOverdueFeeStrategy implements OverdueFeeStrategy {
    private static final BigDecimal FEE_PER_DAY = new BigDecimal("0.5"); // 每天0.5元

    @Override
    public BigDecimal calculateOverdueFee(long overdueDays) {
        return FEE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
    }
}    
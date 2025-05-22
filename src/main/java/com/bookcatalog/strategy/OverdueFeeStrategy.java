package com.bookcatalog.strategy;

import java.math.BigDecimal;

/**
 * 逾期费用策略接口（策略模式）
 */
public interface OverdueFeeStrategy {
    /**
     * 计算逾期费用
     * @param overdueDays 逾期天数
     * @return 逾期费用
     */
    BigDecimal calculateOverdueFee(long overdueDays);
}    
package com.bookcatalog.strategy;

import com.bookcatalog.enums.BookType;

import java.util.HashMap;
import java.util.Map;

/**
 * 逾期费用策略上下文（策略模式）
 */
public class OverdueFeeStrategyContext {
    private static final Map<BookType, OverdueFeeStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put(BookType.PAPER_BOOK, new PaperBookOverdueFeeStrategy());
        STRATEGIES.put(BookType.E_BOOK, new EBookOverdueFeeStrategy());
    }

    /**
     * 获取逾期费用计算策略
     * @param bookType 图书类型
     * @return 对应的策略
     */
    public static OverdueFeeStrategy getStrategy(BookType bookType) {
        if (bookType == null) {
            throw new IllegalArgumentException("图书类型不能为空");
        }
        
        OverdueFeeStrategy strategy = STRATEGIES.get(bookType);
        if (strategy == null) {
            throw new UnsupportedOperationException("不支持的图书类型: " + bookType.getName());
        }
        
        return strategy;
    }
}    
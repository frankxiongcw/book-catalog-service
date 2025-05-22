package com.bookcatalog.observer;

import com.bookcatalog.entity.Book;

/**
 * 图书借出观察者接口（观察者模式）
 */
public interface BookBorrowedObserver {
    /**
     * 更新方法，当图书被借出时调用
     * @param book 图书信息
     */
    void update(Book book);
}    
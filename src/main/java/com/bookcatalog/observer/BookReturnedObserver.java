package com.bookcatalog.observer;

import com.bookcatalog.entity.Book;

/**
 * 图书归还观察者接口（观察者模式）
 */
public interface BookReturnedObserver {
    /**
     * 更新方法，当图书被归还时调用
     * @param book 图书信息
     */
    void update(Book book);
}    
package com.bookcatalog.observer;

import com.bookcatalog.entity.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 图书归还主题（观察者模式）
 */
@Component
public class BookReturnedSubject {
    private List<BookReturnedObserver> observers = new ArrayList<>();

    /**
     * 注册观察者
     * @param observer 观察者
     */
    public void registerObserver(BookReturnedObserver observer) {
        observers.add(observer);
    }

    /**
     * 移除观察者
     * @param observer 观察者
     */
    public void removeObserver(BookReturnedObserver observer) {
        observers.remove(observer);
    }

    /**
     * 通知所有观察者
     * @param book 图书信息
     */
    public void notifyObservers(Book book) {
        for (BookReturnedObserver observer : observers) {
            observer.update(book);
        }
    }
}    
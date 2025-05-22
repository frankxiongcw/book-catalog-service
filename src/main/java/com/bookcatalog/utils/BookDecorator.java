package com.bookcatalog.utils;

import com.bookcatalog.entity.Book;

/**
 * 图书装饰器（装饰器模式）
 */
public class BookDecorator extends Book {
    private final Book book;
    private final String additionalService;

    public BookDecorator(Book book, String additionalService) {
        // 复制原书的属性
        this.setId(book.getId());
        this.setTitle(book.getTitle());
        this.setAuthor(book.getAuthor());
        this.setIsbn(book.getIsbn());
        this.setType(book.getType());
        this.setIsBorrowed(book.getIsBorrowed());
        this.setBorrowDate(book.getBorrowDate());
        this.setReturnDate(book.getReturnDate());
        this.setPrice(book.getPrice());
        this.setFormat(book.getFormat());
        this.setPages(book.getPages());
        
        this.book = book;
        this.additionalService = additionalService;
    }

    @Override
    public String getAdditionalService() {
        return additionalService;
    }
}    
package com.bookcatalog.factory;

import com.bookcatalog.entity.Book;
import com.bookcatalog.enums.BookType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 图书工厂类（工厂模式）
 */
@Service
public class BookFactory {
    /**
     * 创建图书对象
     * @param type 图书类型
     * @param title 书名
     * @param author 作者
     * @param isbn ISBN号
     * @param price 价格
     * @param format 电子书格式
     * @param pages 纸质书页数
     * @return 图书对象
     */
    public Book createBook(Integer type, String title, String author, String isbn, 
                          BigDecimal price, String format, Integer pages) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setType(type);
        book.setPrice(price);
        book.setIsBorrowed(false);
        
        if (BookType.PAPER_BOOK.getCode().equals(type)) {
            // 纸质书特有属性
            book.setPages(pages);
        } else if (BookType.E_BOOK.getCode().equals(type)) {
            // 电子书特有属性
            book.setFormat(format);
        }
        
        return book;
    }
}    
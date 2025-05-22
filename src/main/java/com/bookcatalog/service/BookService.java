package com.bookcatalog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bookcatalog.entity.Book;

import java.util.List;

/**
 * 图书服务接口
 */
public interface BookService extends IService<Book> {
    /**
     * 创建图书
     * @param book 图书信息
     * @return 创建的图书
     */
    Book createBook(Book book);

    /**
     * 更新图书
     * @param id 图书ID
     * @param book 图书信息
     * @return 更新的图书
     */
    Book updateBook(Long id, Book book);

    /**
     * 借阅图书
     * @param id 图书ID
     * @return 操作结果
     */
    Boolean borrowBook(Long id);

    /**
     * 归还图书
     * @param id 图书ID
     * @return 操作结果
     */
    Boolean returnBook(Long id);

    /**
     * 获取所有图书
     * @return 图书列表
     */
    List<Book> getAllBooks();

    /**
     * 获取单本图书
     * @param id 图书ID
     * @return 图书信息
     */
    Book getBookById(Long id);

    /**
     * 删除图书
     * @param id 图书ID
     * @return 操作结果
     */
    Boolean deleteBook(Long id);

    /**
     * 预约图书
     * @param bookId 图书ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Boolean reserveBook(Long bookId, String userId);
}    
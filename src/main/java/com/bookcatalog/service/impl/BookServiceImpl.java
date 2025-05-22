package com.bookcatalog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bookcatalog.dao.BookMapper;
import com.bookcatalog.entity.Book;
import com.bookcatalog.entity.Reservation;
import com.bookcatalog.enums.BookType;
import com.bookcatalog.factory.BookFactory;
import com.bookcatalog.observer.BookBorrowedSubject;
import com.bookcatalog.observer.BookReturnedSubject;
import com.bookcatalog.service.BookService;
import com.bookcatalog.service.ReservationService;
import com.bookcatalog.strategy.OverdueFeeStrategy;
import com.bookcatalog.strategy.OverdueFeeStrategyContext;
import com.bookcatalog.utils.BookDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 图书服务实现类
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private BookFactory bookFactory;

    @Autowired
    private BookBorrowedSubject bookBorrowedSubject;

    @Autowired
    private BookReturnedSubject bookReturnedSubject;

    @Autowired
    private ReservationService reservationService;

    @Override
    @Transactional
    public Book createBook(Book book) {
        // 使用工厂模式创建图书
        Book newBook = bookFactory.createBook(book.getType(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getPrice(), book.getFormat(), book.getPages());

        // 使用装饰器模式添加额外服务
        if (book.getAdditionalService() != null && !book.getAdditionalService().isEmpty()) {
            newBook = new BookDecorator(newBook, book.getAdditionalService());
        }

        this.save(newBook);
        return newBook;
    }

    @Override
    public Book updateBook(Long id, Book book) {
        Book existingBook = this.getById(id);
        if (existingBook == null) {
            return null;
        }

        // 更新基本信息
        if (book.getTitle() != null) existingBook.setTitle(book.getTitle());
        if (book.getAuthor() != null) existingBook.setAuthor(book.getAuthor());
        if (book.getIsbn() != null) existingBook.setIsbn(book.getIsbn());
        if (book.getType() != null) existingBook.setType(book.getType());
        if (book.getPrice() != null) existingBook.setPrice(book.getPrice());
        if (book.getFormat() != null) existingBook.setFormat(book.getFormat());
        if (book.getPages() != null) existingBook.setPages(book.getPages());

        this.updateById(existingBook);
        return existingBook;
    }

    @Override
    @Transactional
    public Boolean borrowBook(Long id) {
        Book book = this.getById(id);
        if (book == null || book.getIsBorrowed()) {
            return false;
        }

        // 更新借阅状态
        book.setIsBorrowed(true);
        book.setBorrowDate(new Date());
        // 默认借14天
        Date returnDate = new Date();
        returnDate.setTime(returnDate.getTime() + 14 * 24 * 60 * 60 * 1000);
        book.setReturnDate(returnDate);

        this.updateById(book);

        // 通知所有观察者图书已被借出
        bookBorrowedSubject.notifyObservers(book);

        return true;
    }

    @Override
    @Transactional
    public Boolean returnBook(Long id) {
        Book book = this.getById(id);
        if (book == null || !book.getIsBorrowed()) {
            return false;
        }

        // 计算逾期费用（如果有）
        BigDecimal overdueFee = calculateOverdueFee(book);
        if (overdueFee.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("图书逾期，需支付费用: " + overdueFee);
            // 这里可以添加处理逾期费用的逻辑
        }

        // 更新借阅状态
        book.setIsBorrowed(false);
        book.setBorrowDate(null);
        book.setReturnDate(null);

        this.updateById(book);

        // 通知所有观察者图书已归还
        bookReturnedSubject.notifyObservers(book);

        // 通知预约用户
        reservationService.notifyReservers(id);

        return true;
    }

    @Override
    public List<Book> getAllBooks() {
        return this.list();
    }

    @Override
    public Book getBookById(Long id) {
        return this.getById(id);
    }

    @Override
    public Boolean deleteBook(Long id) {
        return this.removeById(id);
    }

    @Override
    @Transactional
    public Boolean reserveBook(Long bookId, String userId) {
        Book book = this.getById(bookId);
        if (book == null) {
            return false;
        }

        // 创建预约
        Reservation reservation = new Reservation();
        reservation.setBookId(bookId);
        reservation.setUserId(userId);
        reservation.setReservationDate(new Date());
        reservation.setIsNotified(false);

        reservationService.save(reservation);
        return true;
    }

    /**
     * 计算逾期费用
     * @param book 图书
     * @return 逾期费用
     */
    private BigDecimal calculateOverdueFee(Book book) {
        Date currentDate = new Date();
        if (book.getReturnDate() != null && currentDate.after(book.getReturnDate())) {
            // 计算逾期天数
            long overdueDays = (currentDate.getTime() - book.getReturnDate().getTime()) / (24 * 60 * 60 * 1000);
            
            // 使用策略模式计算逾期费用
            OverdueFeeStrategy strategy = OverdueFeeStrategyContext.getStrategy(BookType.getByCode(book.getType()));
            return strategy.calculateOverdueFee(overdueDays);
        }
        return BigDecimal.ZERO;
    }
}    
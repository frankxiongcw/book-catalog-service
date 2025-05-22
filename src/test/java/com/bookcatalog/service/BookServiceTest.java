package com.bookcatalog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookcatalog.dao.BookMapper;
import com.bookcatalog.entity.Book;
import com.bookcatalog.enums.BookType;
import com.bookcatalog.factory.BookFactory;
import com.bookcatalog.observer.BookBorrowedSubject;
import com.bookcatalog.observer.BookReturnedSubject;
import com.bookcatalog.service.impl.BookServiceImpl;
import com.bookcatalog.strategy.OverdueFeeStrategyContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookFactory bookFactory;

    @Mock
    private BookBorrowedSubject bookBorrowedSubject;

    @Mock
    private BookReturnedSubject bookReturnedSubject;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBook() {
        Book book = new Book();
        book.setTitle("测试图书");
        book.setAuthor("测试作者");
        book.setType(BookType.PAPER_BOOK.getCode());
        book.setPrice(new BigDecimal("99.9"));
        book.setPages(200);

        when(bookFactory.createBook(anyInt(), anyString(), anyString(), anyString(), 
                any(BigDecimal.class), anyString(), anyInt())).thenReturn(book);
        when(bookMapper.insert(any(Book.class))).thenReturn(1);

        Book result = bookService.createBook(book);

        assertNotNull(result);
        assertEquals("测试图书", result.getTitle());
        verify(bookMapper, times(1)).insert(any(Book.class));
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("原书名");
        existingBook.setAuthor("原作者");

        Book updatedBook = new Book();
        updatedBook.setTitle("新书名");
        updatedBook.setAuthor("新作者");

        when(bookMapper.selectById(1L)).thenReturn(existingBook);
        when(bookMapper.updateById(any(Book.class))).thenReturn(1);

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("新书名", result.getTitle());
        assertEquals("新作者", result.getAuthor());
        verify(bookMapper, times(1)).updateById(any(Book.class));
    }

    @Test
    public void testUpdateBookNotFound() {
        when(bookMapper.selectById(1L)).thenReturn(null);

        Book result = bookService.updateBook(1L, new Book());

        assertNull(result);
        verify(bookMapper, never()).updateById(any(Book.class));
    }

    @Test
    public void testBorrowBook() {
        Book book = new Book();
        book.setId(1L);
        book.setIsBorrowed(false);

        when(bookMapper.selectById(1L)).thenReturn(book);
        when(bookMapper.updateById(any(Book.class))).thenReturn(1);

        Boolean result = bookService.borrowBook(1L);

        assertTrue(result);
        assertTrue(book.getIsBorrowed());
        assertNotNull(book.getBorrowDate());
        assertNotNull(book.getReturnDate());
        verify(bookBorrowedSubject, times(1)).notifyObservers(any(Book.class));
    }

    @Test
    public void testBorrowBookAlreadyBorrowed() {
        Book book = new Book();
        book.setId(1L);
        book.setIsBorrowed(true);

        when(bookMapper.selectById(1L)).thenReturn(book);

        Boolean result = bookService.borrowBook(1L);

        assertFalse(result);
        verify(bookMapper, never()).updateById(any(Book.class));
        verify(bookBorrowedSubject, never()).notifyObservers(any(Book.class));
    }

    @Test
    public void testReturnBook() {
        Book book = new Book();
        book.setId(1L);
        book.setIsBorrowed(true);
        book.setReturnDate(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)); // 2天前应归还

        when(bookMapper.selectById(1L)).thenReturn(book);
        when(bookMapper.updateById(any(Book.class))).thenReturn(1);

        // 模拟策略模式返回逾期费用
        when(OverdueFeeStrategyContext.getStrategy(any())).thenReturn(overdueDays -> new BigDecimal("1.0"));

        Boolean result = bookService.returnBook(1L);

        assertTrue(result);
        assertFalse(book.getIsBorrowed());
        assertNull(book.getBorrowDate());
        assertNull(book.getReturnDate());
        verify(bookReturnedSubject, times(1)).notifyObservers(any(Book.class));
        verify(reservationService, times(1)).notifyReservers(1L);
    }

    @Test
    public void testReturnBookNotBorrowed() {
        Book book = new Book();
        book.setId(1L);
        book.setIsBorrowed(false);

        when(bookMapper.selectById(1L)).thenReturn(book);

        Boolean result = bookService.returnBook(1L);

        assertFalse(result);
        verify(bookMapper, never()).updateById(any(Book.class));
        verify(bookReturnedSubject, never()).notifyObservers(any(Book.class));
        verify(reservationService, never()).notifyReservers(anyLong());
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("图书1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("图书2");

        List<Book> books = Arrays.asList(book1, book2);

        when(bookMapper.selectList(any(QueryWrapper.class))).thenReturn(books);

        List<Book> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("测试图书");

        when(bookMapper.selectById(1L)).thenReturn(book);

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("测试图书", result.getTitle());
        verify(bookMapper, times(1)).selectById(1L);
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookMapper.selectById(1L)).thenReturn(null);

        Book result = bookService.getBookById(1L);

        assertNull(result);
        verify(bookMapper, times(1)).selectById(1L);
    }

    @Test
    public void testDeleteBook() {
        when(bookMapper.deleteById(1L)).thenReturn(1);

        Boolean result = bookService.deleteBook(1L);

        assertTrue(result);
        verify(bookMapper, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBookNotFound() {
        when(bookMapper.deleteById(1L)).thenReturn(0);

        Boolean result = bookService.deleteBook(1L);

        assertFalse(result);
        verify(bookMapper, times(1)).deleteById(1L);
    }

    @Test
    public void testReserveBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookMapper.selectById(1L)).thenReturn(book);
        when(reservationService.save(any())).thenReturn(true);

        Boolean result = bookService.reserveBook(1L, "user123");

        assertTrue(result);
        verify(reservationService, times(1)).save(any());
    }

    @Test
    public void testReserveBookNotFound() {
        when(bookMapper.selectById(1L)).thenReturn(null);

        Boolean result = bookService.reserveBook(1L, "user123");

        assertFalse(result);
        verify(reservationService, never()).save(any());
    }
}    
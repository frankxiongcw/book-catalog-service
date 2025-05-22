package com.bookcatalog.controller;

import com.bookcatalog.entity.Book;
import com.bookcatalog.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("测试图书");
        book.setAuthor("测试作者");
        book.setPrice(new BigDecimal("99.9"));

        when(bookService.createBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"测试图书\",\"author\":\"测试作者\",\"price\":99.9}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("测试图书")));
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("图书1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("图书2");

        List<Book> books = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("图书1")))
                .andExpect(jsonPath("$[1].title", is("图书2")));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("测试图书");

        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("测试图书")));
    }

    @Test
    public void testGetBookByIdNotFound() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("更新后的图书");

        when(bookService.updateBook(1L, any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"更新后的图书\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("更新后的图书")));
    }

    @Test
    public void testUpdateBookNotFound() throws Exception {
        when(bookService.updateBook(1L, any(Book.class))).thenReturn(null);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"更新后的图书\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteBook() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testDeleteBookNotFound() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("false"));
    }

    @Test
    public void testBorrowBook() throws Exception {
        when(bookService.borrowBook(1L)).thenReturn(true);

        mockMvc.perform(post("/api/books/1/borrow"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testBorrowBookFailed() throws Exception {
        when(bookService.borrowBook(1L)).thenReturn(false);

        mockMvc.perform(post("/api/books/1/borrow"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("false"));
    }

    @Test
    public void testReturnBook() throws Exception {
        when(bookService.returnBook(1L)).thenReturn(true);

        mockMvc.perform(post("/api/books/1/return"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testReturnBookFailed() throws Exception {
        when(bookService.returnBook(1L)).thenReturn(false);

        mockMvc.perform(post("/api/books/1/return"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("false"));
    }

    @Test
    public void testReserveBook() throws Exception {
        when(bookService.reserveBook(1L, "user123")).thenReturn(true);

        mockMvc.perform(post("/api/books/1/reserve")
                .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testReserveBookFailed() throws Exception {
        when(bookService.reserveBook(1L, "user123")).thenReturn(false);

        mockMvc.perform(post("/api/books/1/reserve")
                .param("userId", "user123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("false"));
    }
}    
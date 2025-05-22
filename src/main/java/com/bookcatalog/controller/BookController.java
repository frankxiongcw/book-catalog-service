package com.bookcatalog.controller;

import com.bookcatalog.entity.Book;
import com.bookcatalog.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书控制器
 */
@RestController
@RequestMapping("/api/books")
@Api(tags = "图书管理", description = "图书目录服务API")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    @ApiOperation(value = "创建图书")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<Book>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "更新图书")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @PostMapping("/{id}/borrow")
    @ApiOperation(value = "借阅图书")
    public ResponseEntity<Boolean> borrowBook(@PathVariable Long id) {
        Boolean result = bookService.borrowBook(id);
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/return")
    @ApiOperation(value = "归还图书")
    public ResponseEntity<Boolean> returnBook(@PathVariable Long id) {
        Boolean result = bookService.returnBook(id);
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ApiOperation(value = "获取所有图书")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取单本图书")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除图书")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
        Boolean result = bookService.deleteBook(id);
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/reserve")
    @ApiOperation(value = "预约图书")
    public ResponseEntity<Boolean> reserveBook(@PathVariable Long id, @RequestParam String userId) {
        Boolean result = bookService.reserveBook(id, userId);
        if (result) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}    
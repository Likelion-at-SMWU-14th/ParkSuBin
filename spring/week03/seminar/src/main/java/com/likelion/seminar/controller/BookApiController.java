package com.likelion.seminar.controller;

import com.likelion.seminar.domain.Book;
import com.likelion.seminar.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookService bookService;

    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    // REST 방식 도서 목록 조회 (JSON 반환)
    @GetMapping
    public List<Book> getBooksApi() {
        return bookService.getAllBooks();
    }

    // REST 방식 도서 등록
    @PostMapping
    public String addBookApi(@RequestParam String title,
                             @RequestParam String author,
                             @RequestParam int price) {
        bookService.addBook(title, author, price);
        return "도서가 성공적으로 등록되었습니다.";
    }
}
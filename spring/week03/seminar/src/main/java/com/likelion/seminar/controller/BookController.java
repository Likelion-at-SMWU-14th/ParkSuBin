package com.likelion.seminar.controller;

import com.likelion.seminar.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 도서 목록 및 등록 페이지 조회 (MVC 방식)
    @GetMapping("/books")
    public String getBooksPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }


    @PostMapping("/books")
    public String addBook(@RequestParam String title,
                          @RequestParam String author,
                          @RequestParam int price) {
        bookService.addBook(title, author, price);
        return "redirect:/books";
    }
}

package com.likelion.seminar.service;

import com.likelion.seminar.domain.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private final List<Book> books = new ArrayList<>();

    // 책 추가 기능
    public void addBook(String title, String author, int price) {
        Book book = new Book(title, author, price);
        books.add(book);
    }

    // 전체 책 목록 조회 기능
    public List<Book> getAllBooks() {
        return books;
    }
}

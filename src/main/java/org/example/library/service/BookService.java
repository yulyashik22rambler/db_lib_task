package org.example.library.service;

import org.example.library.dto.request.CreateBookRequest;
import org.example.library.dto.response.BookDto;

import java.util.List;

public interface BookService {

    /** Задача 2: все книги с авторами и жанрами */
    List<BookDto> getAllBooksDetailed();

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequest request);

    List<BookDto> searchByTitle(String q);

    List<BookDto> findByAuthorLastName(String lastName);

    List<BookDto> findByGenre(String genreName);

    List<BookDto> getAvailableBooks();
}
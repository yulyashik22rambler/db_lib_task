package org.example.library.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.CreateBookRequest;
import org.example.library.dto.response.BookDto;
import org.example.library.dto.response.LoanDto;
import org.example.library.service.BookService;
import org.example.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Каталог книг")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;

    /** Задача 2: все книги с авторами и жанрами */
    @Operation(summary = "Все книги с авторами и жанрами")
    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAllBooksDetailed();
    }

    @Operation(summary = "Книга по id")
    @GetMapping("/{id}")
    public BookDto getById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Поиск книг по названию (подстрока)")
    @GetMapping("/search")
    public List<BookDto> searchByTitle(@RequestParam("title") String title) {
        return bookService.searchByTitle(title);
    }

    @Operation(summary = "Поиск книг по фамилии автора")
    @GetMapping("/by-author")
    public List<BookDto> byAuthor(@RequestParam("lastName") String lastName) {
        return bookService.findByAuthorLastName(lastName);
    }

    @Operation(summary = "Поиск книг по жанру")
    @GetMapping("/by-genre")
    public List<BookDto> byGenre(@RequestParam("name") String name) {
        return bookService.findByGenre(name);
    }

    @Operation(summary = "Только доступные к выдаче книги")
    @GetMapping("/available")
    public List<BookDto> available() {
        return bookService.getAvailableBooks();
    }

    /** Задача 6: история выдачи книги */
    @Operation(summary = "История выдачи книги (все loan-записи)")
    @GetMapping("/{id}/history")
    public List<LoanDto> history(@PathVariable Long id) {
        return loanService.getBookHistory(id);
    }

    @Operation(summary = "Создать книгу")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Valid @RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }
}
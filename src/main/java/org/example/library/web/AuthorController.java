package org.example.library.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.CreateAuthorRequest;
import org.example.library.dto.response.AuthorDto;
import org.example.library.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Управление авторами")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    /** Задача 1: все авторы книг */
    @Operation(summary = "Все авторы с их книгами")
    @GetMapping
    public List<AuthorDto> getAll() {
        return authorService.getAllAuthors();
    }

    @Operation(summary = "Автор по id")
    @GetMapping("/{id}")
    public AuthorDto getById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @Operation(summary = "Поиск авторов по фамилии (префикс)")
    @GetMapping("/search")
    public List<AuthorDto> search(@RequestParam("lastName") String lastName) {
        return authorService.searchByLastName(lastName);
    }

    @Operation(summary = "Создать автора")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@Valid @RequestBody CreateAuthorRequest request) {
        return authorService.createAuthor(request);
    }
}
package org.example.library.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.CreateReaderRequest;
import org.example.library.dto.response.CountDto;
import org.example.library.dto.response.ReaderDto;
import org.example.library.service.LoanService;
import org.example.library.service.ReaderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/readers")
@Tag(name = "Readers", description = "Читатели библиотеки")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;
    private final LoanService loanService;

    /** Задача 4: все читатели */
    @Operation(summary = "Все читатели")
    @GetMapping
    public List<ReaderDto> getAll() {
        return readerService.getAllReaders();
    }

    @Operation(summary = "Читатель по id")
    @GetMapping("/{id}")
    public ReaderDto getById(@PathVariable Long id) {
        return readerService.getReaderById(id);
    }

    @Operation(summary = "Поиск читателей по фамилии (подстрока)")
    @GetMapping("/search")
    public List<ReaderDto> search(@RequestParam("lastName") String lastName) {
        return readerService.searchByLastName(lastName);
    }

    /** Задача 5: сколько книг взято читателем на конкретную дату */
    @Operation(summary = "Сколько книг взято читателем на конкретную дату")
    @GetMapping("/{readerId}/loans/count")
    public CountDto countOnDate(
            @PathVariable Long readerId,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return loanService.countBooksTakenByReaderOnDate(readerId, date);
    }

    @Operation(summary = "Создать читателя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReaderDto create(@Valid @RequestBody CreateReaderRequest request) {
        return readerService.createReader(request);
    }
}
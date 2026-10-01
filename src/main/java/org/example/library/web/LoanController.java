package org.example.library.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.request.IssueBookRequest;
import org.example.library.dto.request.ReturnBookRequest;
import org.example.library.dto.response.LoanDto;
import org.example.library.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Выдача и возврат книг")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    /** Задача 3: книги, выданные на данный момент */
    @Operation(summary = "Активные выдачи (книги на руках)")
    @GetMapping("/active")
    public List<LoanDto> active() {
        return loanService.getActiveLoans();
    }

    /** Задача 7: выдача книги */
    @Operation(summary = "Выдать книгу читателю")
    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDto issue(@Valid @RequestBody IssueBookRequest request) {
        return loanService.issueBook(request);
    }

    @Operation(summary = "Вернуть книгу")
    @PostMapping("/return")
    public LoanDto returnBook(@Valid @RequestBody ReturnBookRequest request) {
        return loanService.returnBook(request);
    }
}
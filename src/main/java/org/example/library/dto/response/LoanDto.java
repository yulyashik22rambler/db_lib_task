package org.example.library.dto.response;

import org.example.library.domain.Author;
import org.example.library.domain.Loan;

import java.time.LocalDate;
import java.util.List;

public record LoanDto(
        Long id,
        Long bookId,
        String bookTitle,
        List<String> bookAuthors,
        Long readerId,
        String readerName,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        String status
) {
    public static LoanDto from(Loan l) {
        return new LoanDto(
                l.getId(),
                l.getBook().getId(),
                l.getBook().getTitle(),
                l.getBook().getAuthors().stream()
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .toList(),
                l.getReader().getId(),
                l.getReader().getFirstName() + " " + l.getReader().getLastName(),
                l.getLoanDate(),
                l.getDueDate(),
                l.getReturnDate(),
                l.getStatus().name()
        );
    }
}
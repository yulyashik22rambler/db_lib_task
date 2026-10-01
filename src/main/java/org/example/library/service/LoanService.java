package org.example.library.service;

import org.example.library.dto.request.IssueBookRequest;
import org.example.library.dto.request.ReturnBookRequest;
import org.example.library.dto.response.CountDto;
import org.example.library.dto.response.LoanDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface LoanService {

    /** Задача 3: книги, выданные на данный момент */
    List<LoanDto> getActiveLoans();

    /** Задача 5: сколько книг взято читателем на дату */
    CountDto countBooksTakenByReaderOnDate(Long readerId, LocalDate date);

    /** Задача 6: история выдачи книги */
    List<LoanDto> getBookHistory(Long bookId);

    /** Задача 7: выдача книги */
    LoanDto issueBook(IssueBookRequest request);

    /** Возврат книги (доп. операция, логично иметь рядом с выдачей) */
    LoanDto returnBook(Long loanId);

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    LoanDto returnBook(ReturnBookRequest req);
}
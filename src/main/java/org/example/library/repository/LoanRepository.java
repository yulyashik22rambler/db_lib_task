package org.example.library.repository;

import org.example.library.domain.Loan;
import org.example.library.domain.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    /**
     * Задача 3: книги, выданные на данный момент (ACTIVE + OVERDUE).
     * JOIN FETCH book и reader — сразу тянем связанные сущности,
     * чтобы DTO не триггерил N+1.
     */
    @Query("""
           select l from Loan l
           join fetch l.book b
           join fetch l.reader r
           where l.status in ('ACTIVE', 'OVERDUE')
           order by l.dueDate
           """)
    List<Loan> findActiveLoans();

    /**
     * Все выдачи конкретного читателя (с книгой) — для истории.
     */
    @Query("""
           select l from Loan l
           join fetch l.book b
           where l.reader.id = :readerId
           order by l.loanDate desc
           """)
    List<Loan> findByReaderIdWithBook(@Param("readerId") Long readerId);

    /**
     * Задача 5: сколько книг взято читателем на конкретную дату.
     */
    @Query("""
           select count(l) from Loan l
           where l.reader.id = :readerId
             and l.loanDate = :date
           """)
    long countByReaderIdAndLoanDate(@Param("readerId") Long readerId,
                                    @Param("date") LocalDate date);

    /**
     * То же, но учитывает все выдачи, начатые до даты и ещё не возвращённые,
     * плюс выдачи, начатые в этот день. Полезно для отчёта «на руках на дату».
     */
    @Query("""
           select count(l) from Loan l
           where l.reader.id = :readerId
             and l.loanDate <= :date
             and (l.returnDate is null or l.returnDate >= :date)
           """)
    long countActiveOnDate(@Param("readerId") Long readerId,
                           @Param("date") LocalDate date);

    /**
     * Задача 6: история выдачи книги с читателем и авторами книги.
     * Используем join fetch, чтобы LoanDto мог собрать reader
     * и book.authors без дополнительных запросов.
     */
    @Query("""
           select distinct l from Loan l
           join fetch l.reader r
           join fetch l.book b
           left join fetch b.authors
           where b.id = :bookId
           order by l.loanDate desc
           """)
    List<Loan> findHistoryByBookId(@Param("bookId") Long bookId);

    /**
     * Найти активную выдачу конкретной книги конкретному читателю.
     * Пригодится для проверки «не держит ли он уже эту книгу».
     */
    @Query("""
           select l from Loan l
           where l.book.id = :bookId
             and l.reader.id = :readerId
             and l.status in ('ACTIVE', 'OVERDUE')
           """)
    Optional<Loan> findActiveByBookAndReader(@Param("bookId") Long bookId,
                                             @Param("readerId") Long readerId);

    /**
     * Просроченные выдачи (для начисления штрафов / отчётов).
     */
    @Query("""
           select l from Loan l
           join fetch l.book
           join fetch l.reader
           where l.status in ('ACTIVE', 'OVERDUE')
             and l.dueDate < :today
           """)
    List<Loan> findOverdue(@Param("today") LocalDate today);

    List<Loan> findByStatus(LoanStatus status);

    long countByBookIdAndStatusIn(Long bookId, List<LoanStatus> statuses);
}
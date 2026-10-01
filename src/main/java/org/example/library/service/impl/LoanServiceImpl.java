package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.library.config.CacheConfig;
import org.example.library.domain.Book;
import org.example.library.domain.Fine;
import org.example.library.domain.Loan;
import org.example.library.domain.Reader;
import org.example.library.domain.enums.LoanStatus;
import org.example.library.dto.request.IssueBookRequest;
import org.example.library.dto.request.ReturnBookRequest;
import org.example.library.dto.response.CountDto;
import org.example.library.dto.response.LoanDto;
import org.example.library.exception.BusinessException;
import org.example.library.exception.EntityNotFoundException;
import org.example.library.exception.NoAvailableCopiesException;
import org.example.library.repository.BookRepository;
import org.example.library.repository.FineRepository;
import org.example.library.repository.LoanRepository;
import org.example.library.repository.ReaderRepository;
import org.example.library.service.LoanService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final FineRepository fineRepository;

    @Override
    public List<LoanDto> getActiveLoans() {
        return loanRepository.findActiveLoans()
                .stream()
                .map(LoanDto::from)
                .toList();
    }

    @Override
    public CountDto countBooksTakenByReaderOnDate(Long readerId, LocalDate date) {
        if (!readerRepository.existsById(readerId)) {
            throw new EntityNotFoundException("Reader not found: " + readerId);
        }
        long count = loanRepository.countByReaderIdAndLoanDate(readerId, date);
        return new CountDto(readerId, date, count);
    }

    @Override
    public List<LoanDto> getBookHistory(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new EntityNotFoundException("Book not found: " + bookId);
        }
        return loanRepository.findHistoryByBookId(bookId)
                .stream()
                .map(LoanDto::from)
                .toList();
    }

    /**
     * Задача 7. Транзакция выдачи книги.
     *
     * Шаги:
     *  1) пессимистично блокируем строку books (SELECT ... FOR UPDATE);
     *  2) проверяем available_copies > 0;
     *  3) уменьшаем available_copies;
     *  4) создаём запись в loans со статусом ACTIVE;
     *  5) коммит.
     *
     * Изоляция READ_COMMITTED + FOR UPDATE гарантируют, что
     * два параллельных запроса не «перепродадут» последнюю копию.
     * rollbackFor = Exception.class — при любой ошибке откатываем всё.
     */
    @Override
    @CacheEvict(cacheNames = CacheConfig.BOOKS_CACHE, allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public LoanDto issueBook(IssueBookRequest req) {
        log.debug("Issuing book {} to reader {} for {} days",
                req.bookId(), req.readerId(), req.days());

        // 1. Блокируем строку книги
        Book book = bookRepository.findByIdForUpdate(req.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + req.bookId()));

        // 2. Проверяем остаток
        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new NoAvailableCopiesException(
                    "No available copies for book: " + book.getTitle());
        }

        // Доп. проверка: не держит ли читатель уже эту книгу (partial unique index в БД)
        Reader reader = readerRepository.findById(req.readerId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found: " + req.readerId()));

        loanRepository.findActiveByBookAndReader(req.bookId(), req.readerId())
                .ifPresent(l -> {
                    throw new BusinessException(
                            "Reader already has this book on loan (loan id=" + l.getId() + ")");
                });

        // 3. Уменьшаем счётчик
        book.setAvailableCopies(book.getAvailableCopies() - 1);

        // 4. Создаём выдачу
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setReader(reader);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(req.days()));
        loan.setStatus(LoanStatus.ACTIVE);

        Loan saved = loanRepository.save(loan);

        // 5. Возвращаем DTO. Транзакция закоммитится после выхода из метода.
        return LoanDto.from(saved);
    }

    @Override
    public LoanDto returnBook(Long loanId) {
        return null;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public LoanDto returnBook(ReturnBookRequest req) {
        log.debug("Returning loan {}, damaged={}, notes={}",
                req.loanId(), req.damaged(), req.notes());

        Loan loan = loanRepository.findById(req.loanId())
                .orElseThrow(() -> new EntityNotFoundException("Loan not found: " + req.loanId()));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException("Loan already returned: " + req.loanId());
        }

        // Блокируем книгу: увеличение availableCopies без блокировки — race condition
        Book book = bookRepository.findByIdForUpdate(loan.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found: " + loan.getBook().getId()));

        LocalDate returnDate = req.returnDate() != null ? req.returnDate() : LocalDate.now();

        if (returnDate.isBefore(loan.getLoanDate())) {
            throw new BusinessException(
                    "Return date " + returnDate + " is before loan date " + loan.getLoanDate());
        }

        loan.setReturnDate(returnDate);
        loan.setStatus(LoanStatus.RETURNED);
        if (req.notes() != null && !req.notes().isBlank()) {
            loan.setNotes(req.notes());
        }

        // Возвращаем копию в фонд (не выше total)
        if (book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }

        // Штраф за повреждение
        if (Boolean.TRUE.equals(req.damaged())) {
            BigDecimal amount = req.damageAmount() != null
                    ? req.damageAmount()
                    : new BigDecimal("500.00");   // дефолтная сумма
            createFine(loan, amount, "Книга повреждена при возврате");
        }

        // Штраф за просрочку (пример: 10 ₽/день)
        long overdueDays = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        if (overdueDays > 0) {
            BigDecimal amount = BigDecimal.valueOf(overdueDays * 10L);
            createFine(loan, amount,
                    "Просрочка возврата на " + overdueDays + " дн.");
        }

        return LoanDto.from(loan);
    }

    private void createFine(Loan loan, BigDecimal amount, String reason) {
        Fine fine = new Fine();
        fine.setLoan(loan);
        fine.setAmount(amount);
        fine.setReason(reason);
        fine.setPaid(false);
        fineRepository.save(fine);
        log.debug("Created fine {} for loan {}: {}", amount, loan.getId(), reason);
    }
}
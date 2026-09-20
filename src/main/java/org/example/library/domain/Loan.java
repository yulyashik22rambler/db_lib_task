package org.example.library.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.library.domain.enums.LoanStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Loan.java
@Entity
@Table(name = "loans",
        indexes = {
                @Index(name = "idx_loans_reader_date", columnList = "reader_id, loan_date"),
                @Index(name = "idx_loans_status", columnList = "status"),
                @Index(name = "idx_loans_book", columnList = "book_id")
        })
@Getter
@Setter
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_loan_book"))
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reader_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_loan_reader"))
    private Reader reader;

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate = LocalDate.now();

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status = LoanStatus.ACTIVE;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fine> fines = new ArrayList<>();
}


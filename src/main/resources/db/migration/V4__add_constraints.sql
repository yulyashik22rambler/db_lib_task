-- ============================================================
-- V4: Ограничения целостности (UNIQUE, CHECK, NOT NULL).
-- ============================================================

-- ---------- Уникальность ----------
ALTER TABLE genres
    ADD CONSTRAINT uq_genres_name UNIQUE (name);

ALTER TABLE books
    ADD CONSTRAINT uq_books_isbn UNIQUE (isbn);

ALTER TABLE readers
    ADD CONSTRAINT uq_readers_email        UNIQUE (email),
    ADD CONSTRAINT uq_readers_library_card UNIQUE (library_card);

-- ---------- CHECK: книги ----------
ALTER TABLE books
    ADD CONSTRAINT chk_books_total_copies     CHECK (total_copies     >= 0),
    ADD CONSTRAINT chk_books_available_copies CHECK (available_copies >= 0),
    ADD CONSTRAINT chk_books_available_le_total
        CHECK (available_copies <= total_copies),
    ADD CONSTRAINT chk_books_publication_year
        CHECK (publication_year IS NULL OR publication_year BETWEEN 1000 AND 2100);

-- ---------- CHECK: читатели ----------
ALTER TABLE readers
    ADD CONSTRAINT chk_readers_email_format
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    ADD CONSTRAINT chk_readers_names_not_empty
        CHECK (length(trim(first_name)) > 0 AND length(trim(last_name)) > 0);

-- ---------- CHECK: выдачи ----------
ALTER TABLE loans
    ADD CONSTRAINT chk_loans_due_after_loan
        CHECK (due_date > loan_date),
    ADD CONSTRAINT chk_loans_return_after_loan
        CHECK (return_date IS NULL OR return_date >= loan_date),
    ADD CONSTRAINT chk_loans_status
        CHECK (status IN ('ACTIVE', 'RETURNED', 'OVERDUE')),
    ADD CONSTRAINT chk_loans_returned_has_date
        CHECK (
            (status = 'RETURNED' AND return_date IS NOT NULL)
            OR (status <> 'RETURNED')
        );

-- ---------- CHECK: штрафы ----------
ALTER TABLE fines
    ADD CONSTRAINT chk_fines_amount_positive CHECK (amount >= 0),
    ADD CONSTRAINT chk_fines_paid_at_consistency
        CHECK (
            (paid = TRUE  AND paid_at IS NOT NULL)
            OR (paid = FALSE AND paid_at IS NULL)
        );

-- ---------- Уникальность активной выдачи ----------
-- Один читатель не может держать одну и ту же книгу дважды одновременно.
-- Реализуем через partial unique index.
CREATE UNIQUE INDEX uq_loans_active_book_reader
    ON loans (book_id, reader_id)
    WHERE status = 'ACTIVE';
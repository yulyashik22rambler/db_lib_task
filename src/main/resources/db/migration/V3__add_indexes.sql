-- ============================================================
-- V3: Индексы для часто используемых запросов.
-- ============================================================

-- ---------- Поиск книг по названию (регистронезависимый) ----------
CREATE INDEX idx_books_title_lower
    ON books (LOWER(title));

-- ---------- Поиск авторов по фамилии ----------
CREATE INDEX idx_authors_last_name_lower
    ON authors (LOWER(last_name));

-- ---------- Поиск книг конкретного автора ----------
-- (junction-таблица: часто ищем "все книги автора X")
CREATE INDEX idx_book_authors_author
    ON book_authors (author_id);

-- ---------- Поиск книг конкретного жанра ----------
CREATE INDEX idx_book_genres_genre
    ON book_genres (genre_id);

-- ---------- История выдач читателя ----------
-- (SELECT ... WHERE reader_id = ? ORDER BY loan_date DESC)
CREATE INDEX idx_loans_reader_date
    ON loans (reader_id, loan_date DESC);

-- ---------- История выдач по книге ----------
CREATE INDEX idx_loans_book_date
    ON loans (book_id, loan_date DESC);

-- ---------- Активные выдачи ----------
-- PARTIAL INDEX: индексируем только ACTIVE — быстрее и меньше
CREATE INDEX idx_loans_status_active
    ON loans (status)
    WHERE status = 'ACTIVE';

-- ---------- Просроченные (для отчётов/штрафов) ----------
CREATE INDEX idx_loans_due_date_active
    ON loans (due_date)
    WHERE status IN ('ACTIVE', 'OVERDUE');

-- ---------- Штрафы по выдаче и по статусу оплаты ----------
CREATE INDEX idx_fines_loan        ON fines (loan_id);
CREATE INDEX idx_fines_unpaid      ON fines (paid) WHERE paid = FALSE;

-- ---------- Поиск по ISBN (уникальный уже есть, но иногда ищем префикс) ----------
CREATE INDEX idx_books_isbn        ON books (isbn);
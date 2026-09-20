-- ============================================================
-- V2: Внешние ключи и правила ссылочной целостности.
--
-- CASCADE  — дочерние записи теряют смысл без родителя
--            (junction-таблицы, штрафы).
-- RESTRICT — родителя нельзя удалить, пока есть ссылки
--            (книга/читатель с историей выдачи).
-- ============================================================

-- ---------- book_authors ----------
ALTER TABLE book_authors
    ADD CONSTRAINT fk_book_authors_book
        FOREIGN KEY (book_id) REFERENCES books (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_book_authors_author
        FOREIGN KEY (author_id) REFERENCES authors (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

-- ---------- book_genres ----------
ALTER TABLE book_genres
    ADD CONSTRAINT fk_book_genres_book
        FOREIGN KEY (book_id) REFERENCES books (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_book_genres_genre
        FOREIGN KEY (genre_id) REFERENCES genres (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;

-- ---------- loans ----------
-- Книгу/читателя, по которым есть история, удалять нельзя.
ALTER TABLE loans
    ADD CONSTRAINT fk_loans_book
        FOREIGN KEY (book_id) REFERENCES books (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    ADD CONSTRAINT fk_loans_reader
        FOREIGN KEY (reader_id) REFERENCES readers (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE;

-- ---------- fines ----------
-- Штраф без выдачи не имеет смысла — CASCADE.
ALTER TABLE fines
    ADD CONSTRAINT fk_fines_loan
        FOREIGN KEY (loan_id) REFERENCES loans (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;
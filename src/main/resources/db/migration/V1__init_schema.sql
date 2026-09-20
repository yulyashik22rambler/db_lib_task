-- ============================================================
-- V1: Базовая схема. Создание таблиц и первичных ключей.
-- Нормализация до 3NF:
--   * junction-таблицы book_authors и book_genres
--   * штрафы вынесены в отдельную таблицу fines
--   * нет транзитивных зависимостей (жанр/автор не в books)
-- ============================================================

-- ---------- Авторы ----------
CREATE TABLE authors (
    id          BIGSERIAL    PRIMARY KEY,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    birth_date  DATE,
    country     VARCHAR(100),
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now()
);

-- ---------- Жанры ----------
CREATE TABLE genres (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT now()
);

-- ---------- Книги ----------
CREATE TABLE books (
    id                BIGSERIAL    PRIMARY KEY,
    title             VARCHAR(255) NOT NULL,
    isbn              VARCHAR(20),
    publication_year  INTEGER,
    publisher         VARCHAR(150),
    total_copies      INTEGER      NOT NULL DEFAULT 0,
    available_copies  INTEGER      NOT NULL DEFAULT 0,
    language          VARCHAR(10)  NOT NULL DEFAULT 'ru',
    created_at        TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT now(),
    version           BIGINT       NOT NULL DEFAULT 0
);

-- ---------- Читатели ----------
CREATE TABLE readers (
    id             BIGSERIAL    PRIMARY KEY,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    phone          VARCHAR(20),
    library_card   VARCHAR(30)  NOT NULL,
    registered_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT now(),
    active         BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ---------- Книга ↔ Авторы (M:N) ----------
CREATE TABLE book_authors (
    book_id    BIGINT NOT NULL,
    author_id  BIGINT NOT NULL,
    PRIMARY KEY (book_id, author_id)
);

-- ---------- Книга ↔ Жанры (M:N) ----------
CREATE TABLE book_genres (
    book_id   BIGINT NOT NULL,
    genre_id  BIGINT NOT NULL,
    PRIMARY KEY (book_id, genre_id)
);

-- ---------- История выдачи книг ----------
CREATE TABLE loans (
    id           BIGSERIAL   PRIMARY KEY,
    book_id      BIGINT      NOT NULL,
    reader_id    BIGINT      NOT NULL,
    loan_date    DATE        NOT NULL DEFAULT CURRENT_DATE,
    due_date     DATE        NOT NULL,
    return_date  DATE,
    status       VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    notes        VARCHAR(500),
    created_at   TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP   NOT NULL DEFAULT now()
);

-- ---------- Штрафы ----------
CREATE TABLE fines (
    id          BIGSERIAL      PRIMARY KEY,
    loan_id     BIGINT         NOT NULL,
    amount      NUMERIC(10, 2) NOT NULL,
    reason      VARCHAR(255),
    paid        BOOLEAN        NOT NULL DEFAULT FALSE,
    paid_at     TIMESTAMP,
    created_at  TIMESTAMP      NOT NULL DEFAULT now()
);

-- ---------- Комментарии к таблицам ----------
COMMENT ON TABLE  authors        IS 'Авторы книг';
COMMENT ON TABLE  genres         IS 'Жанры книг';
COMMENT ON TABLE  books          IS 'Книги в фонде библиотеки';
COMMENT ON TABLE  readers        IS 'Читатели библиотеки';
COMMENT ON TABLE  book_authors   IS 'M:N связь книг и авторов';
COMMENT ON TABLE  book_genres    IS 'M:N связь книг и жанров';
COMMENT ON TABLE  loans          IS 'История выдачи книг';
COMMENT ON TABLE  fines          IS 'Штрафы за просрочку/порчу книг';

COMMENT ON COLUMN books.total_copies     IS 'Всего копий в фонде';
COMMENT ON COLUMN books.available_copies IS 'Доступно к выдаче сейчас';
COMMENT ON COLUMN books.version          IS 'Оптимистичная блокировка (@Version)';
COMMENT ON COLUMN loans.status           IS 'ACTIVE | RETURNED | OVERDUE';
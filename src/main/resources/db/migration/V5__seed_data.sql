-- ============================================================
-- V5: Тестовые данные для локальной разработки.
-- В продакшене отключить через:
--   spring.flyway.locations=classpath:db/migration
--   и вынести seed в отдельный профиль / отдельную папку.
-- ============================================================

-- ---------- Жанры ----------
INSERT INTO genres (name, description) VALUES
    ('Роман',        'Крупное повествовательное произведение'),
    ('Фантастика',   'Научная и ненаучная фантастика'),
    ('Детектив',     'Расследование преступлений'),
    ('Поэзия',       'Стихотворные произведения'),
    ('История',      'Историческая литература'),
    ('Философия',    'Философские трактаты и эссе');

-- ---------- Авторы ----------
INSERT INTO authors (first_name, last_name, birth_date, country) VALUES
    ('Лев',     'Толстой',      '1828-09-09', 'Россия'),
    ('Фёдор',   'Достоевский',  '1821-11-11', 'Россия'),
    ('Антон',   'Чехов',        '1860-01-29', 'Россия'),
    ('Михаил',  'Булгаков',     '1891-05-15', 'Россия'),
    ('Джордж',  'Оруэлл',       '1903-06-25', 'Великобритания'),
    ('Джон',    'Толкин',       '1892-01-03', 'Великобритания'),
    ('Габриэль','Гарсиа Маркес','1927-03-06', 'Колумбия'),
    ('Артур',   'Конан Дойл',   '1859-05-22', 'Великобритания');

-- ---------- Книги ----------
INSERT INTO books (title, isbn, publication_year, publisher, total_copies, available_copies) VALUES
    ('Война и мир',                  '978-5-04-116275-7', 1869, 'Русский вестник',   5, 5),
    ('Анна Каренина',                '978-5-04-116276-4', 1877, 'Русский вестник',   4, 4),
    ('Преступление и наказание',     '978-5-04-116277-1', 1866, 'Русский вестник',   3, 3),
    ('Идиот',                        '978-5-04-116278-8', 1869, 'Русский вестник',   2, 2),
    ('Вишнёвый сад',                 '978-5-04-116279-5', 1904, 'Знание',            3, 3),
    ('Мастер и Маргарита',           '978-5-04-116280-1', 1967, 'Москва',            6, 6),
    ('1984',                         '978-0-45-152493-5', 1949, 'Secker & Warburg',  4, 4),
    ('Скотный двор',                 '978-0-45-152494-2', 1945, 'Secker & Warburg',  2, 2),
    ('Властелин колец',              '978-0-54-792423-3', 1954, 'Allen & Unwin',     5, 5),
    ('Хоббит',                       '978-0-54-792422-6', 1937, 'Allen & Unwin',     3, 3),
    ('Сто лет одиночества',          '978-5-04-116281-8', 1967, 'Harper & Row',      2, 2),
    ('Приключения Шерлока Холмса',   '978-5-04-116282-5', 1892, 'George Newnes',     4, 4);

-- ---------- Книга ↔ Авторы ----------
-- «Война и мир», «Анна Каренина» — Толстой
INSERT INTO book_authors (book_id, author_id) VALUES
    ((SELECT id FROM books WHERE title = 'Война и мир'),         (SELECT id FROM authors WHERE last_name = 'Толстой' AND first_name = 'Лев')),
    ((SELECT id FROM books WHERE title = 'Анна Каренина'),       (SELECT id FROM authors WHERE last_name = 'Толстой' AND first_name = 'Лев')),
    ((SELECT id FROM books WHERE title = 'Преступление и наказание'), (SELECT id FROM authors WHERE last_name = 'Достоевский')),
    ((SELECT id FROM books WHERE title = 'Идиот'),               (SELECT id FROM authors WHERE last_name = 'Достоевский')),
    ((SELECT id FROM books WHERE title = 'Вишнёвый сад'),        (SELECT id FROM authors WHERE last_name = 'Чехов')),
    ((SELECT id FROM books WHERE title = 'Мастер и Маргарита'),  (SELECT id FROM authors WHERE last_name = 'Булгаков')),
    ((SELECT id FROM books WHERE title = '1984'),                (SELECT id FROM authors WHERE last_name = 'Оруэлл')),
    ((SELECT id FROM books WHERE title = 'Скотный двор'),        (SELECT id FROM authors WHERE last_name = 'Оруэлл')),
    ((SELECT id FROM books WHERE title = 'Властелин колец'),     (SELECT id FROM authors WHERE last_name = 'Толкин')),
    ((SELECT id FROM books WHERE title = 'Хоббит'),              (SELECT id FROM authors WHERE last_name = 'Толкин')),
    ((SELECT id FROM books WHERE title = 'Сто лет одиночества'), (SELECT id FROM authors WHERE last_name = 'Гарсиа Маркес')),
    ((SELECT id FROM books WHERE title = 'Приключения Шерлока Холмса'), (SELECT id FROM authors WHERE last_name = 'Конан Дойл'));

-- ---------- Книга ↔ Жанры ----------
INSERT INTO book_genres (book_id, genre_id) VALUES
    ((SELECT id FROM books WHERE title = 'Война и мир'),              (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Война и мир'),              (SELECT id FROM genres WHERE name = 'История')),
    ((SELECT id FROM books WHERE title = 'Анна Каренина'),            (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Преступление и наказание'), (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Преступление и наказание'), (SELECT id FROM genres WHERE name = 'Философия')),
    ((SELECT id FROM books WHERE title = 'Идиот'),                    (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Вишнёвый сад'),             (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Мастер и Маргарита'),       (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Мастер и Маргарита'),       (SELECT id FROM genres WHERE name = 'Фантастика')),
    ((SELECT id FROM books WHERE title = '1984'),                     (SELECT id FROM genres WHERE name = 'Фантастика')),
    ((SELECT id FROM books WHERE title = '1984'),                     (SELECT id FROM genres WHERE name = 'Философия')),
    ((SELECT id FROM books WHERE title = 'Скотный двор'),             (SELECT id FROM genres WHERE name = 'Фантастика')),
    ((SELECT id FROM books WHERE title = 'Властелин колец'),          (SELECT id FROM genres WHERE name = 'Фантастика')),
    ((SELECT id FROM books WHERE title = 'Хоббит'),                   (SELECT id FROM genres WHERE name = 'Фантастика')),
    ((SELECT id FROM books WHERE title = 'Сто лет одиночества'),      (SELECT id FROM genres WHERE name = 'Роман')),
    ((SELECT id FROM books WHERE title = 'Приключения Шерлока Холмса'), (SELECT id FROM genres WHERE name = 'Детектив'));

-- ---------- Читатели ----------
INSERT INTO readers (first_name, last_name, email, phone, library_card) VALUES
    ('Иван',    'Иванов',     'ivanov@example.com',    '+7-900-111-11-11', 'LIB-0001'),
    ('Мария',   'Петрова',    'petrova@example.com',   '+7-900-222-22-22', 'LIB-0002'),
    ('Алексей', 'Сидоров',    'sidorov@example.com',   '+7-900-333-33-33', 'LIB-0003'),
    ('Ольга',   'Кузнецова',  'kuznetsova@example.com','+7-900-444-44-44', 'LIB-0004'),
    ('Дмитрий', 'Смирнов',    'smirnov@example.com',   '+7-900-555-55-55', 'LIB-0005');

-- ---------- Выдачи ----------
-- Активная выдача: книга «1984» у Иванова
INSERT INTO loans (book_id, reader_id, loan_date, due_date, status)
VALUES (
    (SELECT id FROM books   WHERE title = '1984'),
    (SELECT id FROM readers WHERE library_card = 'LIB-0001'),
    CURRENT_DATE - INTERVAL '5 days',
    CURRENT_DATE + INTERVAL '9 days',
    'ACTIVE'
);

-- Возвращённая выдача: «Мастер и Маргарита» у Петровой
INSERT INTO loans (book_id, reader_id, loan_date, due_date, return_date, status)
VALUES (
    (SELECT id FROM books   WHERE title = 'Мастер и Маргарита'),
    (SELECT id FROM readers WHERE library_card = 'LIB-0002'),
    CURRENT_DATE - INTERVAL '30 days',
    CURRENT_DATE - INTERVAL '16 days',
    CURRENT_DATE - INTERVAL '18 days',
    'RETURNED'
);

-- Просроченная выдача: «Властелин колец» у Сидорова
INSERT INTO loans (book_id, reader_id, loan_date, due_date, status)
VALUES (
    (SELECT id FROM books   WHERE title = 'Властелин колец'),
    (SELECT id FROM readers WHERE library_card = 'LIB-0003'),
    CURRENT_DATE - INTERVAL '40 days',
    CURRENT_DATE - INTERVAL '12 days',
    'OVERDUE'
);

-- Синхронизируем available_copies с фактическими выдачами
UPDATE books
SET available_copies = total_copies - (
    SELECT COUNT(*) FROM loans
    WHERE loans.book_id = books.id AND loans.status IN ('ACTIVE', 'OVERDUE')
);

-- ---------- Штрафы ----------
-- Штраф за просрочку «Властелина колец»
INSERT INTO fines (loan_id, amount, reason, paid)
VALUES (
    (SELECT id FROM loans
       WHERE book_id = (SELECT id FROM books WHERE title = 'Властелин колец')
         AND status = 'OVERDUE'),
    150.00,
    'Просрочка возврата книги',
    FALSE
);
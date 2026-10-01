package org.example.library.repository;

import jakarta.persistence.LockModeType;
import org.example.library.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Задача 2: все книги с авторами и жанрами одним запросом.
     * JOIN FETCH × 2 — иначе получим N+1 (для каждой книги отдельный
     * select на авторов и ещё один на жанры).
     */
    @Query("""
           select distinct b from Book b
           left join fetch b.authors
           left join fetch b.genres
           order by b.title
           """)
    List<Book> findAllWithAuthorsAndGenres();

    /**
     * Одна книга с авторами и жанрами.
     */
    @Query("""
           select distinct b from Book b
           left join fetch b.authors
           left join fetch b.genres
           where b.id = :id
           """)
    Optional<Book> findByIdWithAuthorsAndGenres(@Param("id") Long id);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    /**
     * Поиск книг по подстроке в названии (регистронезависимый).
     */
    @Query("""
           select b from Book b
           where lower(b.title) like lower(concat('%', :q, '%'))
           order by b.title
           """)
    List<Book> searchByTitle(@Param("q") String query);

    /**
     * Поиск книг по фамилии автора.
     */
    @Query("""
           select distinct b from Book b
           join b.authors a
           where lower(a.lastName) like lower(concat('%', :lastName, '%'))
           order by b.title
           """)
    List<Book> findByAuthorLastName(@Param("lastName") String lastName);

    /**
     * Поиск книг по жанру.
     */
    @Query("""
           select distinct b from Book b
           join b.genres g
           where lower(g.name) = lower(:genreName)
           order by b.title
           """)
    List<Book> findByGenreName(@Param("genreName") String genreName);

    /**
     * Только книги, доступные к выдаче (available_copies > 0).
     */
    @Query("""
           select distinct b from Book b
           left join fetch b.authors
           left join fetch b.genres
           where b.availableCopies > 0
           order by b.title
           """)
    List<Book> findAllAvailable();

    /**
     * Пессимистичная блокировка строки книги для операции выдачи.
     *
     * Гарантирует, что два параллельных запроса не увидят одну
     * и ту же available_copies > 0 и не «перепродадут» последнюю копию.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") Long id);

    /**
     * Количество доступных копий (для быстрой проверки без загрузки всей книги).
     */
    @Query("select b.availableCopies from Book b where b.id = :id")
    Optional<Integer> findAvailableCopiesById(@Param("id") Long id);
}
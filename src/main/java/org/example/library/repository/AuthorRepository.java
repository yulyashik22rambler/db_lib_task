package org.example.library.repository;

import org.example.library.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Все авторы с подгруженными книгами (JOIN FETCH — избегаем N+1).
     */
    @Query("""
           select distinct a from Author a
           left join fetch a.books
           """)
    List<Author> findAllWithBooks();

    /**
     * Автор с подгруженными книгами по id.
     */
    @Query("""
           select distinct a from Author a
           left join fetch a.books
           where a.id = :id
           """)
    Optional<Author> findByIdWithBooks(@Param("id") Long id);

    /**
     * Поиск авторов по фамилии (регистронезависимый, префикс).
     */
    @Query("""
           select a from Author a
           where lower(a.lastName) like lower(concat(:prefix, '%'))
           order by a.lastName, a.firstName
           """)
    List<Author> findByLastNameStartingWithIgnoreCase(@Param("prefix") String prefix);

    /**
     * Все авторы, отсортированные по фамилии.
     */
    List<Author> findAllByOrderByLastNameAscFirstNameAsc();

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
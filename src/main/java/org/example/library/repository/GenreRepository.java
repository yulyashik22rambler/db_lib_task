package org.example.library.repository;

import org.example.library.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    /**
     * Все жанры с подгруженными книгами.
     */
    @Query("""
           select distinct g from Genre g
           left join fetch g.books
           """)
    List<Genre> findAllWithBooks();

    List<Genre> findAllByOrderByNameAsc();
}

package org.example.library.repository;

import org.example.library.domain.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Optional<Reader> findByEmailIgnoreCase(String email);

    Optional<Reader> findByLibraryCard(String libraryCard);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByLibraryCard(String libraryCard);

    /**
     * Все читатели, отсортированные по фамилии, затем по имени.
     * Derived query — Spring Data сам сгенерирует JPQL по имени метода.
     */
    List<Reader> findAllByOrderByLastNameAscFirstNameAsc();

    /**
     * Только активные читатели (поле active = true в сущности Reader).
     * Если поля active нет — удалите этот метод или добавьте поле.
     */
    List<Reader> findByActiveTrueOrderByLastNameAscFirstNameAsc();

    /**
     * Читатель с историей выдач.
     */
    @Query("""
           select distinct r from Reader r
           left join fetch r.loans
           where r.id = :id
           """)
    Optional<Reader> findByIdWithLoans(@Param("id") Long id);

    /**
     * Поиск читателей по подстроке в фамилии.
     */
    @Query("""
           select r from Reader r
           where lower(r.lastName) like lower(concat('%', :q, '%'))
           order by r.lastName, r.firstName
           """)
    List<Reader> searchByLastName(@Param("q") String query);
}
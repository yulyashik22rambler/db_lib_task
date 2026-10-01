package org.example.library.repository;

import org.example.library.domain.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    List<Fine> findByLoanId(Long loanId);

    List<Fine> findByPaidFalse();

    @Query("""
           select f from Fine f
           join fetch f.loan l
           join fetch l.reader
           where l.reader.id = :readerId
           order by f.createdAt desc
           """)
    List<Fine> findByReaderId(@Param("readerId") Long readerId);

    @Query("""
           select coalesce(sum(f.amount), 0) from Fine f
           where f.loan.reader.id = :readerId
             and f.paid = false
           """)
    BigDecimal sumUnpaidByReaderId(@Param("readerId") Long readerId);

    long countByPaidFalse();
}
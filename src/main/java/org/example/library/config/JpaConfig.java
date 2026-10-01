package org.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Аудит JPA-сущностей и управление транзакциями.
 *
 * Аудит:
 *  - @CreatedDate       на поле сущности → проставляется при INSERT
 *  - @LastModifiedDate  на поле сущности → проставляется при INSERT и UPDATE
 *
 * Чтобы это заработало, в сущностях нужны аннотации:
 *   @EntityListeners(AuditingEntityListener.class)
 *   @Column(name = "created_at", nullable = false, updatable = false)
 *   @CreatedDate
 *   private LocalDateTime createdAt;
 *
 *   @Column(name = "updated_at", nullable = false)
 *   @LastModifiedDate
 *   private LocalDateTime updatedAt;
 *
 * DateTimeProvider возвращает LocalDateTime (а не Instant/Date),
 * чтобы совпадало с типом TIMESTAMP в PostgreSQL.
 */
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableTransactionManagement
public class JpaConfig {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider auditingDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}
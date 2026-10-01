package org.example.library.dto.request;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * Расширенный запрос возврата:
 *  - loanId       — какую выдачу закрываем;
 *  - returnDate   — дата возврата (по умолчанию сегодня, если null);
 *  - notes        — заметка библиотекаря;
 *  - damaged      — книга повреждена → создаём штраф;
 *  - damageAmount — сумма штрафа за повреждение (опционально).
 */
public record ReturnBookRequest(
        @NotNull Long loanId,
        @PastOrPresent LocalDate returnDate,
        String notes,
        Boolean damaged,
        java.math.BigDecimal damageAmount
) {}
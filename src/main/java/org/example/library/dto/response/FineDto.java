package org.example.library.dto.response;

import jakarta.persistence.Column;
import org.example.library.domain.Fine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FineDto(
        Long id,
        Long loanId,
        BigDecimal amount,
        String reason,
        boolean paid,
        LocalDateTime createdAt
) {
    public static FineDto from(Fine f) {
        return new FineDto(
                f.getId(), f.getLoan().getId(), f.getAmount(),
                f.getReason(), f.getPaid(), f.getCreatedAt()
        );
    }
}
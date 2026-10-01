package org.example.library.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Set;

public record CreateBookRequest(
        @NotBlank String title,
        String isbn,
        Integer publicationYear,
        String publisher,
        @PositiveOrZero int totalCopies,
        Set<Long> authorIds,
        Set<Long> genreIds
) {}
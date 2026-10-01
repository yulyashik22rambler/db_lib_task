package org.example.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record IssueBookRequest(
        @NotNull Long bookId,
        @NotNull Long readerId,
        @Min(1) int days
) {}
package org.example.library.dto.request;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record IssueBookRequest(
        @JsonProperty
        @NotNull Long bookId,
        @JsonProperty
        @NotNull Long readerId,
        @JsonProperty
        @Min(1) int days
) {}
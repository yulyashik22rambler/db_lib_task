package org.example.library.dto.response;

import java.time.LocalDate;

public record CountDto(Long readerId, LocalDate date, long count) {}
package org.example.library.dto.response;

import org.example.library.domain.Genre;

public record GenreDto(Long id, String name) {
    public static GenreDto from(Genre g) {
        return new GenreDto(g.getId(), g.getName());
    }
}
package org.example.library.dto.response;

import org.example.library.domain.Author;

public record AuthorDto(
        Long id,
        String firstName,
        String lastName,
        String country
) {
    public static AuthorDto from(Author a) {
        return new AuthorDto(a.getId(), a.getFirstName(), a.getLastName(), a.getCountry());
    }
}
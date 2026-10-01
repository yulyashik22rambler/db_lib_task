package org.example.library.dto.response;

import org.example.library.domain.Author;
import org.example.library.domain.Book;
import org.example.library.domain.Genre;

import java.util.List;

public record BookDto(
        Long id,
        String title,
        String isbn,
        Integer publicationYear,
        String publisher,
        int totalCopies,
        int availableCopies,
        List<AuthorDto> authors,
        List<GenreDto> genres
) {
    public static BookDto from(Book b) {
        return new BookDto(
                b.getId(),
                b.getTitle(),
                b.getIsbn(),
                b.getPublicationYear(),
                b.getPublisher(),
                b.getTotalCopies(),
                b.getAvailableCopies(),
                b.getAuthors().stream().map(AuthorDto::from).toList(),
                b.getGenres().stream().map(GenreDto::from).toList()
        );
    }
}
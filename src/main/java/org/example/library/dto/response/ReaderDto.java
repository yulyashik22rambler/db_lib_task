package org.example.library.dto.response;

import org.example.library.domain.Reader;

public record ReaderDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String libraryCard
) {
    public static ReaderDto from(Reader r) {
        return new ReaderDto(
                r.getId(), r.getFirstName(), r.getLastName(),
                r.getEmail(), r.getPhone(), r.getLibraryCard()
        );
    }
}
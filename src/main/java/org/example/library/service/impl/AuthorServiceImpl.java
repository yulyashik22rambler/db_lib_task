package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.domain.Author;
import org.example.library.dto.request.CreateAuthorRequest;
import org.example.library.dto.response.AuthorDto;
import org.example.library.exception.BusinessException;
import org.example.library.exception.EntityNotFoundException;
import org.example.library.repository.AuthorRepository;
import org.example.library.service.AuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAllWithBooks()
                .stream()
                .map(AuthorDto::from)
                .toList();
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findByIdWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found: " + id));
        return AuthorDto.from(author);
    }

    @Override
    @Transactional
    public AuthorDto createAuthor(CreateAuthorRequest req) {
        if (authorRepository.existsByFirstNameAndLastName(req.firstName(), req.lastName())) {
            throw new BusinessException("Author already exists: " + req.firstName() + " " + req.lastName());
        }
        Author a = new Author();
        a.setFirstName(req.firstName());
        a.setLastName(req.lastName());
        a.setBirthDate(req.birthDate());
        a.setCountry(req.country());
        return AuthorDto.from(authorRepository.save(a));
    }

    @Override
    public List<AuthorDto> searchByLastName(String prefix) {
        return authorRepository.findByLastNameStartingWithIgnoreCase(prefix)
                .stream()
                .map(AuthorDto::from)
                .toList();
    }
}
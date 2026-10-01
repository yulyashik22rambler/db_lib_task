package org.example.library.service;

import org.example.library.dto.request.CreateAuthorRequest;
import org.example.library.dto.response.AuthorDto;

import java.util.List;

public interface AuthorService {

    List<AuthorDto> getAllAuthors();

    AuthorDto getAuthorById(Long id);

    AuthorDto createAuthor(CreateAuthorRequest request);

    List<AuthorDto> searchByLastName(String prefix);
}
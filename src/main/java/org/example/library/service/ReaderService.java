package org.example.library.service;

import org.example.library.dto.request.CreateReaderRequest;
import org.example.library.dto.response.ReaderDto;

import java.util.List;

public interface ReaderService {

    /** Задача 4: все читатели */
    List<ReaderDto> getAllReaders();

    ReaderDto getReaderById(Long id);

    ReaderDto createReader(CreateReaderRequest request);

    List<ReaderDto> searchByLastName(String q);
}
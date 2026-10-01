package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.domain.Reader;
import org.example.library.dto.request.CreateReaderRequest;
import org.example.library.dto.response.ReaderDto;
import org.example.library.exception.BusinessException;
import org.example.library.exception.EntityNotFoundException;
import org.example.library.repository.ReaderRepository;
import org.example.library.service.ReaderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    @Override
    public List<ReaderDto> getAllReaders() {
        return readerRepository.findAllByOrderByLastNameAscFirstNameAsc()
                .stream()
                .map(ReaderDto::from)
                .toList();
    }

    @Override
    public ReaderDto getReaderById(Long id) {
        Reader r = readerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reader not found: " + id));
        return ReaderDto.from(r);
    }

    @Override
    @Transactional
    public ReaderDto createReader(CreateReaderRequest req) {
        if (readerRepository.existsByEmailIgnoreCase(req.email())) {
            throw new BusinessException("Reader with email already exists: " + req.email());
        }
        if (readerRepository.existsByLibraryCard(req.libraryCard())) {
            throw new BusinessException("Reader with library card already exists: " + req.libraryCard());
        }
        Reader r = new Reader();
        r.setFirstName(req.firstName());
        r.setLastName(req.lastName());
        r.setEmail(req.email());
        r.setPhone(req.phone());
        r.setLibraryCard(req.libraryCard());
        r.setActive(true);
        return ReaderDto.from(readerRepository.save(r));
    }

    @Override
    public List<ReaderDto> searchByLastName(String q) {
        return readerRepository.searchByLastName(q)
                .stream()
                .map(ReaderDto::from)
                .toList();
    }
}
package org.example.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.library.domain.Fine;
import org.example.library.dto.response.FineDto;
import org.example.library.exception.EntityNotFoundException;
import org.example.library.repository.FineRepository;
import org.example.library.service.FineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;

    @Override
    public List<FineDto> getUnpaidFines() {
        return fineRepository.findByPaidFalse()
                .stream()
                .map(FineDto::from)
                .toList();
    }

    @Override
    public List<FineDto> getFinesByReader(Long readerId) {
        return fineRepository.findByReaderId(readerId)
                .stream()
                .map(FineDto::from)
                .toList();
    }

    @Override
    public BigDecimal sumUnpaidByReader(Long readerId) {
        return fineRepository.sumUnpaidByReaderId(readerId);
    }

    @Override
    @Transactional
    public FineDto markPaid(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new EntityNotFoundException("Fine not found: " + fineId));
        fine.setPaid(true);
        fine.setPaidAt(LocalDateTime.now());
        return FineDto.from(fine);
    }
}
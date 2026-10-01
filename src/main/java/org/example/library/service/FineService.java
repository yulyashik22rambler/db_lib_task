package org.example.library.service;

import org.example.library.dto.response.FineDto;

import java.math.BigDecimal;
import java.util.List;

public interface FineService {

    List<FineDto> getUnpaidFines();

    List<FineDto> getFinesByReader(Long readerId);

    BigDecimal sumUnpaidByReader(Long readerId);

    FineDto markPaid(Long fineId);
}
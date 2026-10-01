package org.example.library.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.library.dto.response.FineDto;
import org.example.library.service.FineService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/fines")
@Tag(name = "Fines", description = "Штрафы")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @Operation(summary = "Все неоплаченные штрафы")
    @GetMapping("/unpaid")
    public List<FineDto> unpaid() {
        return fineService.getUnpaidFines();
    }

    @Operation(summary = "Штрафы читателя")
    @GetMapping("/by-reader/{readerId}")
    public List<FineDto> byReader(@PathVariable Long readerId) {
        return fineService.getFinesByReader(readerId);
    }

    @Operation(summary = "Сумма неоплаченных штрафов читателя")
    @GetMapping("/by-reader/{readerId}/sum")
    public BigDecimal sumUnpaid(@PathVariable Long readerId) {
        return fineService.sumUnpaidByReader(readerId);
    }

    @Operation(summary = "Отметить штраф как оплаченный")
    @PostMapping("/{fineId}/pay")
    public FineDto pay(@PathVariable Long fineId) {
        return fineService.markPaid(fineId);
    }
}
package model.dto;

import java.time.LocalDate;

public record BorrowDto(
        Long id,
        Long bookId,
        Long personId,
        LocalDate borrowDate,
        LocalDate returnDate
) {
}

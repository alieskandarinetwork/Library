package model.dto.create;

public record CreateBorrowDto(
        Long bookId,
        Long personId
) {
}

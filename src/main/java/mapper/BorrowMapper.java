package mapper;

import model.dto.BorrowDto;
import model.dto.create.CreateBorrowDto;
import model.entity.Borrow;

public class BorrowMapper implements BaseMapper<Borrow, BorrowDto> {
    @Override
    public BorrowDto toDto(Borrow borrow) {
        return new BorrowDto(
                borrow.getId(),
                borrow.getBookId(),
                borrow.getPersonId(),
                borrow.getBorrowDate(),
                borrow.getReturnDate()
        );
    }

    @Override
    public Borrow toEntity(BorrowDto dto) {
        return Borrow.builder()
                .id(dto.id())
                .bookId(dto.bookId())
                .personId(dto.personId())
                .borrowDate(dto.borrowDate())
                .returnDate(dto.returnDate())
                .build();
    }

    public Borrow toEntity(CreateBorrowDto dto) {
        return Borrow.builder()
                .bookId(dto.bookId())
                .personId(dto.personId())
                .build();
    }
}

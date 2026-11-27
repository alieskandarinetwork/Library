package controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.BorrowMapper;
import model.dto.BorrowDto;
import model.dto.create.CreateBorrowDto;
import model.entity.Borrow;
import service.BorrowService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final BorrowMapper borrowMapper;

    public BorrowDto create(CreateBorrowDto dto) {
        try {
            log.info("Creating new borrow ");

            Borrow borrow = borrowMapper.toEntity(dto);
            Borrow savedBorrow = borrowService.save(borrow);

            log.info("Created Borrow {}", savedBorrow.getId());

            return borrowMapper.toDto(savedBorrow);
        } catch (SQLException e) {
            log.error("Database error while creating borrow", e);
            throw new RuntimeException("ثبت امانت کتاب با خطا مواجه شد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to create borrow: {}", e.getMessage());
            throw e;
        }
    }

    public Optional<BorrowDto> findById(Long id) {
        try {
            log.info("Finding borrow {} By Id", id);

            Optional<Borrow> borrow = borrowService.findById(id);

            return borrow.map(borrowMapper::toDto);
        } catch (SQLException e) {
            log.error("Database error while finding borrow {}", id);
            throw new RuntimeException("خطا در دریافت اطلاعات امانت", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find borrow: {}", e.getMessage());
            throw e;
        }
    }

    public List<BorrowDto> findAll() {
        try {
            log.info("Finding all borrows");

            List<BorrowDto> borrows = borrowService.findAll()
                    .stream()
                    .map(borrowMapper::toDto)
                    .toList();

            log.info("Found {} borrows", borrows.size());
            return borrows;
        } catch (SQLException e) {
            log.error("Database error while finding borrows", e);
            throw new RuntimeException("خطا در دریافت لیست امانت‌ها", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find borrows: {}", e.getMessage());
            throw e;
        }
    }

    public Optional<BorrowDto> findActiveBorrowByBookId(Long bookId) {
        try {
            log.info("Finding Borrow By Book Id {}", bookId);

            Optional<Borrow> borrow =
                    borrowService.findActiveBorrowByBookId(bookId);

            log.info("Search Completed {}", bookId);
            return borrow.map(borrowMapper::toDto);
        } catch (SQLException e) {
            log.error("Database error while finding borrow {}", bookId);
            throw new RuntimeException("خطا در دریافت امانت فعال کتاب", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find borrow: {}", e.getMessage());
            throw e;
        }
    }

    public BorrowDto returnBook(Long bookId) {
        try {
            log.info("Returning borrow {}", bookId);
            Borrow returnBorrow = borrowService.returnBook(bookId);

            log.info("Returned borrow {}", bookId);

            return borrowMapper.toDto(returnBorrow);
        } catch (SQLException e) {
            log.error("Database error while returning borrow {}", bookId);
            throw new IllegalArgumentException("ثبت بازگشت کتاب با خطا مواجه شد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to return borrow: {}", e.getMessage());
            throw e;
        }
    }

    public BorrowDto update(BorrowDto borrowDto) {
        try {
            log.info("Updating borrow {}", borrowDto.id());

            Borrow borrow = borrowMapper.toEntity(borrowDto);
            Borrow updateborrow = borrowService.update(borrow);

            log.info("Updated borrow {}", updateborrow.getId());

            return borrowMapper.toDto(updateborrow);
        } catch (SQLException e) {
            log.error("Database error while updating borrow {}");
            throw new IllegalArgumentException("خطا در ویرایش اطلاعات امانت", e);
        } catch (RuntimeException e) {
            log.warn("Failed to update borrow: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteById(Long id) {
        try {
            log.info("Deleting borrow {}", id);

            borrowService.deleteById(id);

            log.info("Deleted borrow {}", id);
        } catch (SQLException e) {
            log.error("Database error while deleting borrow {}", id);
            throw new IllegalArgumentException("خطا در حذف اطلاعات امانت", e);
        } catch (RuntimeException e) {
            log.warn("Failed to delete borrow: {}", e.getMessage());
            throw e;
        }
    }
}

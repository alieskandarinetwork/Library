package service.impl;

import lombok.AllArgsConstructor;
import model.entity.Book;
import model.entity.BookStatus;
import model.entity.Borrow;
import repository.BookRepository;
import repository.BorrowRepository;
import repository.PersonRepository;
import service.BorrowService;
import validation.BorrowValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Override
    public Optional<Borrow> findActiveBorrowByBookId(Long bookId) throws SQLException {
        if (bookId == null) {
            throw new IllegalArgumentException("شناسه کتاب نمیتواند خالی باشد");
        }
        return borrowRepository.findActiveBorrowByBookId(bookId);
    }

    @Override
    public Borrow returnBook(Long bookId) throws SQLException {
        if (bookId == null) {
            throw new IllegalArgumentException(
                    "شناسه کتاب نمی‌تواند خالی باشد"
            );
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("کتاب مورد نظر پیدا نشد"));

        Borrow borrow = borrowRepository
                .findActiveBorrowByBookId(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("امانت داده نشده است"));

        borrow.setReturnDate(LocalDate.now());

        Borrow updateBorrow = borrowRepository.update(borrow);

        if (updateBorrow == null) {
            throw new IllegalArgumentException(" ثبت بازگشت کتاب با خطا مواجه شد ");
        }
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.update(book);
        return updateBorrow;

    }

    @Override
    public Borrow save(Borrow borrow) throws SQLException {
        if (borrow == null) {
            throw new IllegalArgumentException("اطلاعات امانت نباید خالی باشد");
        }
        if (borrow.getBookId() == null || borrow.getPersonId() == null) {
            throw new IllegalArgumentException("شناسه کتاب و شخص الزامی است");
        }
        Book book = bookRepository.findById(borrow.getBookId())
                .orElseThrow(() ->
                        new IllegalArgumentException("کتاب مورد نظر پیدا نشد"));

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new IllegalArgumentException("کتاب در حال حاضر قابل امانت نمی باشد");
        }
        personRepository.findById(borrow.getPersonId())
                .orElseThrow(() ->
                        new IllegalArgumentException("شخحص در سیستم ثبت نشده است"));

        if (borrowRepository.findActiveBorrowByBookId(borrow.getBookId()).isPresent()) {
            throw new IllegalArgumentException("این کتاب امانت داده شده است");
        }

        borrow.setBorrowDate(LocalDate.now());

        borrow.setReturnDate(null);

        BorrowValidator.validate(borrow);

        Borrow savedBorrow = borrowRepository.save(borrow);
        book.setStatus(BookStatus.BORROWED);
        Book updateBook = bookRepository.update(book);
        if (updateBook == null) {
            throw new IllegalArgumentException("به‌روزرسانی وضعیت کتاب ناموفق بود");
        }
        return savedBorrow;

    }

    @Override
    public Optional<Borrow> findById(Long id) throws SQLException {
        return borrowRepository.findById(id);
    }

    @Override
    public List<Borrow> findAll() throws SQLException {
        return borrowRepository.findAll();
    }

    @Override
    public Borrow update(Borrow borrow) throws SQLException {
        BorrowValidator.validate(borrow);
        return borrowRepository.update(borrow);
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        borrowRepository.deleteById(id);
    }
}

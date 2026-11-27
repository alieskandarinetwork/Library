package service.impl;

import lombok.AllArgsConstructor;
import model.entity.Book;
import model.entity.BookStatus;
import repository.BookRepository;
import repository.BorrowRepository;
import service.BookService;
import validation.BookValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;


    @Override
    public List<Book> findAllOrderByTitle() throws SQLException {
        return bookRepository.findAllOrderByTitle();
    }

    @Override
    public List<Book> search(String title, String author, Integer publishYear, BookStatus status) throws SQLException {
        return bookRepository.search(title, author, publishYear, status);
    }

    @Override
    public Book save(Book book) throws SQLException {
        BookValidator.validate(book);

        book.setStatus(BookStatus.AVAILABLE);

        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findById(Long id) throws SQLException {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findAll() throws SQLException {
        return bookRepository.findAll();
    }

    @Override
    public Book update(Book book) throws SQLException {
        Optional<Book> books = bookRepository.findById(book.getId());

        Book existingBook = books
                .orElseThrow(() ->
                        new IllegalArgumentException("کتاب مورد نظر پیدا نشد"));

        book.setIsbn(existingBook.getIsbn());

        BookValidator.validate(book);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPublishYear(book.getPublishYear());

        return bookRepository.update(existingBook);
    }

    @Override
    public void deleteById(Long id) throws SQLException {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("کتاب مورد نظر پیدا نشد"));

        if (book.getStatus() == BookStatus.BORROWED ||
                borrowRepository.findActiveBorrowByBookId(id).isPresent()) {
            throw new IllegalStateException(
                    "امکان حذف کتاب امانت‌داده‌شده وجود ندارد"
            );
        }
        bookRepository.deleteById(id);

    }
}

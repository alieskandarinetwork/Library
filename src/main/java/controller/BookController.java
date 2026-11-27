package controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.BookMapper;
import model.dto.BookDto;
import model.dto.create.CreateBookDto;
import model.entity.Book;
import model.entity.BookStatus;
import service.BookService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookDto create(CreateBookDto dto) {
        try {
            log.info("Creating a new book");

            Book book = bookMapper.toEntity(dto);
            Book savedBook = bookService.save(book);

            log.info("Book created successfully. ID: {}", savedBook.getId());

            return bookMapper.toDto(savedBook);
        } catch (SQLException e) {
            log.error("Database error while creating book", e);
            throw new RuntimeException("کتاب ثبت نشد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to create book", e);
            throw e;
        }
    }

    public Optional<BookDto> findById(Long id) {
        try {
            log.info("Finding a book by ID: {}", id);

            Optional<Book> book = bookService.findById(id);
            log.info("Book search completed. ID: {}", id);

            return book.map(bookMapper::toDto);
        } catch (SQLException e) {
            log.error("Database error while finding book id: {}", id, e);
            throw new RuntimeException("کتاب دریافت نشد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find book : {}", e.getMessage());
            throw e;
        }
    }


    public List<BookDto> findAll() {
        try {
            List<BookDto> books = bookService.findAll()
                    .stream()
                    .map(bookMapper::toDto)
                    .toList();

            log.info(" finding {} books", books.size());
            return books;
        } catch (SQLException e) {
            log.error("Database error while finding books", e);
            throw new RuntimeException("خطا در دریافت لیست کتاب‌ها", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find books: {}", e.getMessage());
            throw e;
        }
    }

    public List<BookDto> findAllOrderByTitle() {
        try {
            log.info(
                    "Finding all books by title"
            );
            List<BookDto> books = bookService.findAllOrderByTitle()
                    .stream()
                    .map(bookMapper::toDto)
                    .toList();

            log.info(" finding {} books by title", books.size());

            return books;
        } catch (SQLException e) {
            log.error("Database error while sorting books", e);
            throw new RuntimeException("خطا در مرتب‌سازی کتاب‌ها", e);
        } catch (RuntimeException e) {
            log.warn("Failed to sort books: {}", e.getMessage());
            throw e;
        }
    }

    public List<BookDto> search(String title, String author, Integer publishYear, BookStatus status) {
        try {
            log.info(
                    "Searching books By Title: {}, Author: {}, Year: {}, Status: {}",
                    title, author, publishYear, status
            );
            List<BookDto> books = bookService
                    .search(title, author, publishYear, status)
                    .stream()
                    .map(bookMapper::toDto)
                    .toList();

            log.info("Found Books {} ", books.size());
            return books;
        } catch (SQLException e) {
            log.error("Database error while searching books", e);
            throw new RuntimeException("خطا در جستجوی کتاب‌ها", e);
        } catch (RuntimeException e) {
            log.warn("Failed to search books: {}", e.getMessage());
            throw e;
        }
    }

    public BookDto update(BookDto bookDto) {
        try {
            log.info("Updating book: {}", bookDto.id());
            Book book = bookMapper.toEntity(bookDto);
            Book updateBook = bookService.update(book);

            log.info("Book updated successfully. ID: {}", updateBook.getId());
            return bookMapper.toDto(updateBook);
        } catch (SQLException e) {
            log.error("Database error while updating book", e);
            throw new RuntimeException("خطا در ویرایش کتاب", e);
        } catch (RuntimeException e) {
            log.warn("Failed to update book: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteById(Long id) {
        try {
            log.info("Deleting book: {}", id);
            bookService.deleteById(id);

            log.info("Book deleted successfully : {}", id);
        } catch (SQLException e) {
            log.error("Database error while deleting book. ID: {}", id, e);
            throw new RuntimeException("خطا در حذف کتاب", e);
        } catch (RuntimeException e) {
            log.warn("Failed to delete book: {}", e.getMessage());
            throw e;
        }
    }
}
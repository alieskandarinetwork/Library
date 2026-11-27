package repository;

import model.entity.Book;
import model.entity.BookStatus;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends BaseRepository<Book, Long> {
    List<Book> findAllOrderByTitle() throws SQLException;

    List<Book> search(String title, String author, Integer publishYear, BookStatus status) throws SQLException;
}

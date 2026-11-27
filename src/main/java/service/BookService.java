package service;

import model.entity.Book;
import model.entity.BookStatus;

import java.sql.SQLException;
import java.util.List;

public interface BookService extends BaseService<Book , Long>{
    List<Book> findAllOrderByTitle() throws SQLException;

    List<Book> search(String title, String author, Integer publishYear, BookStatus status) throws SQLException;
}

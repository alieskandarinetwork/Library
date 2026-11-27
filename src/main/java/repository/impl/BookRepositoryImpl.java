package repository.impl;

import config.ConnectionProvider;
import lombok.AllArgsConstructor;
import model.entity.Book;
import model.entity.BookStatus;
import repository.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository, AutoCloseable {

    private final ConnectionProvider connectionProvider;
    private Connection connection;
    private PreparedStatement preparedStatement;

    @Override
    public List<Book> findAllOrderByTitle() throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT * FROM BOOK WHERE IS_DELETED = 0 ORDER BY TITLE"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            int year = resultSet.getInt("PUBLISH_YEAR");

            Book book = Book.builder()
                    .id(resultSet.getLong("ID"))
                    .title(resultSet.getString("TITLE"))
                    .author(resultSet.getString("AUTHOR"))
                    .isbn(resultSet.getString("ISBN"))
                    .publishYear(resultSet.wasNull() ? null : year)
                    .status(BookStatus.valueOf(resultSet.getString("STATUS")))
                    .build();

            books.add(book);
        }
        resultSet.close();
        return books;
    }

    @Override
    public List<Book> search(String title, String author, Integer publishYear, BookStatus status) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT * FROM BOOK WHERE IS_DELETED = 0"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            int year = resultSet.getInt("PUBLISH_YEAR");

            Book book = Book.builder()
                    .id(resultSet.getLong("ID"))
                    .title(resultSet.getString("TITLE"))
                    .author(resultSet.getString("AUTHOR"))
                    .isbn(resultSet.getString("ISBN"))
                    .publishYear(resultSet.wasNull() ? null : year)
                    .status(BookStatus.valueOf(resultSet.getString("STATUS")))
                    .build();

            if ((title == null || title.isBlank() ||
                    book.getTitle().toLowerCase().contains(title.toLowerCase()))
                    &&
                    (author == null || author.isBlank() ||
                            book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                    &&
                    (publishYear == null || (book.getPublishYear() != null &&
                            book.getPublishYear().equals(publishYear)))
                    &&
                    (status == null || book.getStatus() == status)) {

                books.add(book);
            }
        }
        resultSet.close();
        return books;
    }

    @Override
    public Book save(Book entity) throws SQLException {
        connection = connectionProvider.getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT BOOK_SEQ.nextval AS NEXT_ID FROM dual"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        entity.setId(resultSet.getLong("NEXT_ID"));
        resultSet.close();

        entity.setStatus(BookStatus.AVAILABLE);

        preparedStatement = connection.prepareStatement(
                "insert into BOOK (ID, TITLE, AUTHOR, ISBN, PUBLISH_YEAR, STATUS, IS_DELETED) values (?,?,?,?,?,?,?)"
        );
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.setString(2, entity.getTitle());
        preparedStatement.setString(3, entity.getAuthor());
        preparedStatement.setString(4, entity.getIsbn());
        preparedStatement.setInt(5, entity.getPublishYear());
        preparedStatement.setString(6, entity.getStatus().name());
        preparedStatement.setInt(7, 0);

        preparedStatement.executeUpdate();
        return entity;
    }

    @Override
    public Optional<Book> findById(Long id) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "select * from BOOK where id = ? and IS_DELETED = 0"
        );
        preparedStatement.setLong(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        Book book = null;
        if (resultSet.next()) {
            int year = resultSet.getInt("PUBLISH_YEAR");

            book = Book.builder()
                    .id(resultSet.getLong("ID"))
                    .title(resultSet.getString("TITLE"))
                    .author(resultSet.getString("AUTHOR"))
                    .isbn(resultSet.getString("ISBN"))
                    .status(BookStatus.valueOf(resultSet.getString("STATUS")))
                    .publishYear(resultSet.wasNull() ? null : year)
                    .build();
        }
        resultSet.close();
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT * FROM BOOK WHERE IS_DELETED = 0");

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Book> books = new ArrayList<>();

        while (resultSet.next()) {
            int year = resultSet.getInt("PUBLISH_YEAR");

            Book book = Book.builder()
                    .id(resultSet.getLong("ID"))
                    .title(resultSet.getString("TITLE"))
                    .author(resultSet.getString("AUTHOR"))
                    .isbn(resultSet.getString("ISBN"))
                    .status(BookStatus.valueOf(resultSet.getString("STATUS")))
                    .publishYear(resultSet.wasNull() ? null : year)
                    .build();

            books.add(book);
        }
        resultSet.close();
        return books;
    }


    @Override
    public Book update(Book entity) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PUBLISH_YEAR = ? WHERE ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setString(2, entity.getAuthor());
        preparedStatement.setObject(3, entity.getPublishYear());
        preparedStatement.setLong(4, entity.getId());

        int rows = preparedStatement.executeUpdate();
        if (rows > 0) {
            return entity;
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "update BOOK set IS_DELETED = 1 where ID = ? and IS_DELETED = 0 and STATUS = ?"
        );
        preparedStatement.setLong(1, id);
        preparedStatement.setString(2, BookStatus.AVAILABLE.name());

        int rows = preparedStatement.executeUpdate();
        if (rows == 0) {
            throw new SQLException("کتاب یافت نشد یا در حال حاضر به امانت گرفته شده است");
        }
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}

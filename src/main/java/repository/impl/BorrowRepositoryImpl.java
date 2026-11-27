package repository.impl;

import config.ConnectionProvider;
import lombok.AllArgsConstructor;
import model.entity.Borrow;
import repository.BorrowRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BorrowRepositoryImpl implements BorrowRepository, AutoCloseable {

    private final ConnectionProvider connectionProvider;
    private Connection connection;
    private PreparedStatement preparedStatement;


    @Override
    public Optional<Borrow> findActiveBorrowByBookId(Long bookId) throws SQLException {
        connection = connectionProvider.getConnection();
        preparedStatement = connection.prepareStatement(
                "SELECT * FROM BORROW WHERE BOOK_ID = ? AND RETURN_DATE IS NULL AND IS_DELETED = 0"
        );
        preparedStatement.setLong(1, bookId);
        ResultSet resultSet = preparedStatement.executeQuery();

        Borrow borrow = null;
        if (resultSet.next()) {
            borrow = Borrow.builder()
                    .id(resultSet.getLong("ID"))
                    .bookId(resultSet.getLong("BOOK_ID"))
                    .personId(resultSet.getLong("PERSON_ID"))
                    .borrowDate(resultSet.getDate("BORROW_DATE").toLocalDate())
                    .returnDate(
                            resultSet.getDate("RETURN_DATE") == null
                                    ? null
                                    : resultSet.getDate("RETURN_DATE").toLocalDate()
                    )
                    .build();
        }
        resultSet.close();
        return Optional.ofNullable(borrow);
    }

    @Override
    public Borrow save(Borrow entity) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT BORROW_SEQ.nextval AS NEXT_ID FROM dual"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        entity.setId(resultSet.getLong("NEXT_ID"));
        resultSet.close();

        preparedStatement = connection.prepareStatement(
                "insert into BORROW (ID, BOOK_ID, PERSON_ID, BORROW_DATE, RETURN_DATE, IS_DELETED) VALUES (?,?,?,?,?,0)"
        );

        preparedStatement.setLong(1, entity.getId());
        preparedStatement.setLong(2, entity.getBookId());
        preparedStatement.setLong(3, entity.getPersonId());
        preparedStatement.setDate(4, Date.valueOf(entity.getBorrowDate()));
        preparedStatement.setDate(5,
                entity.getReturnDate() == null ? null : Date.valueOf(entity.getReturnDate()));

        preparedStatement.executeUpdate();
        return entity;
    }

    @Override
    public Optional<Borrow> findById(Long id) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "select * from BORROW where ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setLong(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        Borrow borrow = null;
        if (resultSet.next()) {
            borrow = Borrow.builder()
                    .id(resultSet.getLong("ID"))
                    .bookId(resultSet.getLong("BOOK_ID"))
                    .personId(resultSet.getLong("PERSON_ID"))
                    .borrowDate(resultSet.getDate("BORROW_DATE").toLocalDate())
                    .returnDate(resultSet.getDate
                            ("RETURN_DATE") == null ? null : resultSet.getDate("RETURN_DATE").toLocalDate())
                    .build();
        }
        resultSet.close();
        return Optional.ofNullable(borrow);
    }

    @Override
    public List<Borrow> findAll() throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "select * from BORROW WHERE IS_DELETED = 0"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Borrow> borrows = new ArrayList<>();

        while (resultSet.next()) {
            Borrow borrow = Borrow.builder()
                    .id(resultSet.getLong("ID"))
                    .bookId(resultSet.getLong("BOOK_ID"))
                    .personId(resultSet.getLong("PERSON_ID"))
                    .borrowDate(resultSet.getDate("BORROW_DATE").toLocalDate())
                    .returnDate(resultSet.getDate
                            ("RETURN_DATE") == null ? null : resultSet.getDate("RETURN_DATE").toLocalDate())
                    .build();
            borrows.add(borrow);
        }
        resultSet.close();
        return borrows;

    }

    @Override
    public Borrow update(Borrow entity) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE BORROW set BOOK_ID = ? , PERSON_ID = ? , BORROW_DATE = ?" +
                        " , RETURN_DATE = ? where ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setLong(1, entity.getBookId());
        preparedStatement.setLong(2, entity.getPersonId());
        preparedStatement.setDate(3, Date.valueOf(entity.getBorrowDate()));
        preparedStatement.setDate
                (4, entity.getReturnDate() == null ? null : Date.valueOf(entity.getReturnDate()));
        preparedStatement.setLong(5, entity.getId());
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
                "UPDATE BORROW SET IS_DELETED = 1 WHERE ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setLong(1, id);

        int rows = preparedStatement.executeUpdate();

        if (rows == 0) {
            throw new SQLException("امانت مورد نظر یافت نشد");
        }
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}

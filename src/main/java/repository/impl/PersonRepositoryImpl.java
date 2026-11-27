package repository.impl;

import config.ConnectionProvider;
import lombok.AllArgsConstructor;
import model.entity.Person;
import repository.PersonRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PersonRepositoryImpl implements PersonRepository, AutoCloseable {

    private final ConnectionProvider connectionProvider;
    private Connection connection;
    private PreparedStatement preparedStatement;

    @Override
    public Optional<Person> findByNationalCode(String nationalCode) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON WHERE NATIONAL_CODE = ? AND IS_DELETED = 0"
        );
        preparedStatement.setString(1, nationalCode);

        ResultSet resultSet = preparedStatement.executeQuery();
        Person person = null;

        if (resultSet.next()) {
            person = Person.builder()
                    .id(resultSet.getLong("ID"))
                    .name(resultSet.getString("NAME"))
                    .family(resultSet.getString("FAMILY"))
                    .nationalCode(resultSet.getString("NATIONAL_CODE"))
                    .build();
        }
        resultSet.close();
        return Optional.ofNullable(person);
    }

    @Override
    public Person save(Person entity) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT PERSON_SEQ.nextval AS NEXT_ID FROM dual"
        );
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        entity.setId(resultSet.getLong("NEXT_ID"));

        resultSet.close();

        preparedStatement = connection.prepareStatement(
                "insert into PERSON (ID, NAME, FAMILY, NATIONAL_CODE, IS_DELETED) VALUES (?,?,?,?,0)"
        );
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setString(3, entity.getFamily());
        preparedStatement.setString(4, entity.getNationalCode());
        preparedStatement.executeUpdate();
        return entity;
    }

    @Override
    public Optional<Person> findById(Long id) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON WHERE ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Person person = null;
        if (resultSet.next()) {
            person = Person.builder()
                    .id(resultSet.getLong("ID"))
                    .name(resultSet.getString("NAME"))
                    .family(resultSet.getString("FAMILY"))
                    .nationalCode(resultSet.getString("NATIONAL_CODE"))
                    .build();
        }
        resultSet.close();
        return Optional.ofNullable(person);
    }

    @Override
    public List<Person> findAll() throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "SELECT * FROM PERSON WHERE IS_DELETED = 0"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Person> persons = new ArrayList<>();
        while (resultSet.next()) {
            Person person = Person.builder()
                    .id(resultSet.getLong("ID"))
                    .name(resultSet.getString("NAME"))
                    .family(resultSet.getString("FAMILY"))
                    .nationalCode(resultSet.getString("NATIONAL_CODE"))
                    .build();
            persons.add(person);
        }
        resultSet.close();
        return persons;
    }

    @Override
    public Person update(Person entity) throws SQLException {
        connection = connectionProvider.getConnection();

        preparedStatement = connection.prepareStatement(
                "UPDATE PERSON SET NAME = ?, FAMILY = ?, NATIONAL_CODE = ? WHERE ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getFamily());
        preparedStatement.setString(3, entity.getNationalCode());
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
                "UPDATE PERSON SET IS_DELETED = 1 WHERE ID = ? AND IS_DELETED = 0"
        );
        preparedStatement.setLong(1, id);
        int rows = preparedStatement.executeUpdate();

        if (rows == 0) {
            throw new SQLException("شخص مورد نظر یافت نشد");
        }
    }

    @Override
    public void close() throws Exception {
        preparedStatement.close();
        connection.close();
    }
}

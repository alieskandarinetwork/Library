package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, I> {
    T save(T t) throws SQLException;

    Optional<T> findById(I i) throws SQLException;

    List<T> findAll() throws SQLException;

    T update(T t) throws SQLException;

    void deleteById(I i) throws SQLException;
}

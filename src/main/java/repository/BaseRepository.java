package repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, I> {
    T save(T entity) throws SQLException;

    Optional<T> findById(I id) throws SQLException;

    List<T> findAll() throws SQLException;

    T update(T entity) throws SQLException;

    void deleteById(I id) throws SQLException;
}

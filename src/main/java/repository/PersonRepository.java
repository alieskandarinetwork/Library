package repository;

import model.entity.Person;

import java.sql.SQLException;
import java.util.Optional;

public interface PersonRepository extends BaseRepository<Person, Long> {
    Optional<Person> findByNationalCode(String nationalCode) throws SQLException;
}

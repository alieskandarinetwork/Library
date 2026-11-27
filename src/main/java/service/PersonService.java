package service;
import model.entity.Person;

import java.sql.SQLException;
import java.util.Optional;


public interface PersonService extends BaseService<Person, Long> {
    Optional<Person> findByNationalCode(String nationalCode) throws SQLException;
}

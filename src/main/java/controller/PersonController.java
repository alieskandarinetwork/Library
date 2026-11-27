package controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mapper.PersonMapper;
import model.dto.PersonDto;
import model.dto.create.CreatePersonDto;
import model.entity.Person;
import service.PersonService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;
    private final PersonMapper personMapper;

    public PersonDto create(CreatePersonDto dto) {
        try {
            log.info("Creating new person");

            Person person = personMapper.toEntity(dto);
            Person savedPerson = personService.save(person);

            log.info("Saved person {} ", savedPerson.getId());
            return personMapper.toDto(savedPerson);
        } catch (SQLException e) {
            log.error("Database error while creating person", e);
            throw new RuntimeException("شخص ثبت نشد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to create person: {}", e.getMessage());
            throw e;
        }
    }

    public Optional<PersonDto> findById(Long id) {
        try {
            log.info("Finding person by id {}", id);
            Optional<Person> person = personService.findById(id);

            log.info("Found person {}", person);
            return person.map(personMapper::toDto);
        } catch (SQLException e) {
            log.error("Database error while finding person {}", id);
            throw new RuntimeException("شخص پیدا نشد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find person: {}", e.getMessage());
            throw e;
        }
    }

    public Optional<PersonDto> findByNationalCode(String nationalCode) {
        try {
            log.info("Finding person by NationalCode {}", nationalCode);

            Optional<Person> person =
                    personService.findByNationalCode(nationalCode);

            log.info("Found person {}", person);
            return person.map(personMapper::toDto);

        } catch (SQLException e) {
            log.error("Database error while finding person by national code", e);
            throw new RuntimeException("شخص با این کد ملی پیدا نشد", e);

        } catch (RuntimeException e) {
            log.warn("Failed to find person: {}", e.getMessage());
            throw e;
        }
    }

    public List<PersonDto> findAll() {
        try {
            log.info("Finding all persons");

            List<PersonDto> persons = personService.findAll()
                    .stream()
                    .map(personMapper::toDto)
                    .toList();
            log.info("Found persons {}", persons.size());

            return persons;
        } catch (SQLException e) {
            log.error("Database error while finding persons", e);
            throw new RuntimeException("لیست اشخاص پیدا نشد", e);
        } catch (RuntimeException e) {
            log.warn("Failed to find persons: {}", e.getMessage());
            throw e;
        }
    }

    public PersonDto update(PersonDto personDto) {
        try {
            log.info("Updating person {}", personDto.id());

            Person person = personMapper.toEntity(personDto);
            Person updatePerson = personService.update(person);
            log.info("Updated person {}", updatePerson.getId());

            return personMapper.toDto(updatePerson);
        } catch (SQLException e) {
            log.error("Database error while updating person", e);
            throw new RuntimeException("خطا در ویرایش اطلاعاتات", e);
        } catch (RuntimeException e) {
            log.warn("Failed to update person: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteById(Long id) {
        try {
            log.info("Deleting person {}", id);

            personService.deleteById(id);
            log.info("Deleted person {}", id);
        } catch (SQLException e) {
            log.error("Database error while deleting person {}", id, e);
            throw new RuntimeException("خطا در حذف شخص", e);
        } catch (RuntimeException e) {
            log.warn("Failed to delete person: {}", e.getMessage());
            throw e;
        }
    }


}

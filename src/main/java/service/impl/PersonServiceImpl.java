package service.impl;

import lombok.AllArgsConstructor;
import model.entity.Borrow;
import model.entity.Person;
import repository.BorrowRepository;
import repository.PersonRepository;
import service.PersonService;
import validation.PersonValidator;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final BorrowRepository borrowRepository;


    @Override
    public Optional<Person> findByNationalCode(String nationalCode) throws SQLException {
        return personRepository.findByNationalCode(nationalCode);
    }

    @Override
    public Person save(Person person) throws SQLException {
        PersonValidator.validate(person);

        if (personRepository.findByNationalCode(person.getNationalCode()).isPresent()) {
            throw new IllegalArgumentException("شخصی با این کد ملی قبلاً ثبت شده است");
        }
        return personRepository.save(person);
    }

    @Override
    public Optional<Person> findById(Long id) throws SQLException {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> findAll() throws SQLException {
        return personRepository.findAll();
    }

    @Override
    public Person update(Person person) throws SQLException {
        PersonValidator.validate(person);

        Person exsitingPerson = personRepository.findById(person.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("شخص مورد نظر پیدا نشد"));

        Optional<Person> nationalCode =
                personRepository.findByNationalCode(person.getNationalCode());

        if (nationalCode.isPresent() && !nationalCode.get().getId().equals(person.getId())) {
            throw new IllegalArgumentException("این کد ملی قبلاً برای شخص دیگری ثبت شده است");
        }
        exsitingPerson.setName(person.getName());
        exsitingPerson.setFamily(person.getFamily());
        exsitingPerson.setNationalCode(person.getNationalCode());
        return personRepository.update(exsitingPerson);
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        Person person = personRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("شخص مورد نظر پیدا نشد"));

        List<Borrow> borrows = borrowRepository.findAll();

        for (Borrow borrow : borrows) {
            if (borrow.getPersonId().equals(person.getId()) && borrow.getReturnDate() == null) {
                throw new IllegalArgumentException("شخص دارای امانت فعال است و امکان حذف او وجود ندارد");
            }
        }
        personRepository.deleteById(id);
    }
}

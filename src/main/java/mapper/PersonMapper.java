package mapper;

import model.dto.PersonDto;
import model.dto.create.CreatePersonDto;
import model.entity.Person;

public class PersonMapper implements BaseMapper<Person, PersonDto> {
    @Override
    public PersonDto toDto(Person person) {
        return new PersonDto(
                person.getId(),
                person.getName(),
                person.getFamily(),
                person.getNationalCode()
        );
    }
    @Override
    public Person toEntity(PersonDto dto) {
        return Person.builder()
                .id(dto.id())
                .name(dto.name())
                .family(dto.family())
                .nationalCode(dto.nationalCode())
                .build();
    }

    public Person toEntity(CreatePersonDto dto) {
        return Person.builder()
                .name(dto.name())
                .family(dto.family())
                .nationalCode(dto.nationalCode())
                .build();
    }
}

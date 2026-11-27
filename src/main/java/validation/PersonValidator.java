package validation;

import model.entity.Person;

public class PersonValidator {

    public static void validate(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("اطلاعات شخص نمی‌تواند خالی باشد");
        }

        if (person.getName() == null || person.getName().isBlank()) {
            throw new IllegalArgumentException("نام شخص الزامی است");
        }

        if (person.getNationalCode() == null || person.getNationalCode().isBlank()) {
            throw new IllegalArgumentException("کد ملی الزامی است");
        }
    }
}

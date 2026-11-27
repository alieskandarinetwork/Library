package validation;

import model.entity.Book;


public class BookValidator {
    public static void validate(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("کتاب نمی‌تواند خالی باشد");
        }

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("عنوان کتاب الزامی است");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("نام نویسنده الزامی است");
        }

        if (book.getIsbn() == null || book.getIsbn().isBlank()) {
            throw new IllegalArgumentException("isbn کتاب الزامی است");
        }

        if (book.getPublishYear() == null || book.getPublishYear() <= 0) {
            throw new IllegalArgumentException("سال انتشار کتاب معتبر نیست");
        }
    }
}

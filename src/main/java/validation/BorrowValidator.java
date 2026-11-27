package validation;

import model.entity.Borrow;

public class BorrowValidator {
    public static void validate(Borrow borrow) {
        if (borrow == null) {
            throw new IllegalArgumentException("اطلاعات امانت نمی‌تواند خالی باشد");
        }
        if (borrow.getBorrowDate() == null) {
            throw new IllegalArgumentException("تاریخ امانت الزامی است");
        }
        if (borrow.getReturnDate() != null &&
                borrow.getReturnDate().isBefore(borrow.getBorrowDate())) {
            throw new IllegalArgumentException("تاریخ بازگشت نمی‌تواند قبل از تاریخ امانت باشد");
        }
    }
}


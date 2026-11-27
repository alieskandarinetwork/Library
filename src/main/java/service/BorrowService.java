package service;

import model.entity.Borrow;

import java.sql.SQLException;
import java.util.Optional;

public interface BorrowService extends BaseService<Borrow , Long>{

    Optional<Borrow> findActiveBorrowByBookId(Long bookId) throws SQLException;

    Borrow returnBook(Long bookId) throws SQLException;
}

package repository;


import model.entity.Borrow;

import java.sql.SQLException;
import java.util.Optional;

public interface BorrowRepository extends BaseRepository<Borrow, Long> {
    Optional<Borrow> findActiveBorrowByBookId(Long bookId) throws SQLException;
}

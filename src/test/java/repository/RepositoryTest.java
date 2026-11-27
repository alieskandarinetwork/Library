package repository;

import config.ConnectionProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.impl.BookRepositoryImpl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RepositoryTest {
    @Test
    @DisplayName("تست ساخت ریپازیتوری")
    void testRepository() {
        BookRepositoryImpl repository =
                new BookRepositoryImpl(
                        new ConnectionProvider(), null, null
                );
        assertNotNull(repository);
    }

}

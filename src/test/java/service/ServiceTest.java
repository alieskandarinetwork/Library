package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.impl.BookServiceImpl;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceTest {
    @Test
    @DisplayName("تست ساخت سرویس")
    void testService() {
        BookServiceImpl service =
                new BookServiceImpl(null, null);

        assertNotNull(service);
    }
}

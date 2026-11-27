package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ControllerTest {
    @Test
    @DisplayName("تست کنترلر")
    void testController() {
        BookController controller =
                new BookController(null, null);

        assertNotNull(controller);
    }


}

package model.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Borrow {

    private Long id;
    private Long bookId;
    private Long personId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

}

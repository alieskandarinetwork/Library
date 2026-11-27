package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class Book {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BookStatus status;
    private Integer publishYear;


}

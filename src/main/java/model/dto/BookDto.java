package model.dto;


import model.entity.BookStatus;

public record BookDto(
        Long id,
        String title,
        String author,
        String isbn,
        BookStatus status,
        Integer publishYear
) {
}

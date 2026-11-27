package model.dto.create;

public record CreateBookDto(
        String title,
        String author,
        String isbn,
        Integer publishYear
) {
}

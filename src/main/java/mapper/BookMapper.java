package mapper;

import model.dto.BookDto;
import model.dto.create.CreateBookDto;
import model.entity.Book;

public class BookMapper implements BaseMapper<Book, BookDto> {

    @Override
    public BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getStatus(), book.getPublishYear()
        );
    }

    @Override
    public Book toEntity(BookDto dto) {
        return Book.builder()
                .id(dto.id())
                .title(dto.title())
                .author(dto.author())
                .isbn(dto.isbn())
                .status(dto.status())
                .publishYear(dto.publishYear())
                .build();
    }

    public Book toEntity(CreateBookDto dto) {
        return Book.builder()
                .title(dto.title())
                .author(dto.author())
                .isbn(dto.isbn())
                .publishYear(dto.publishYear())
                .build();
    }
}

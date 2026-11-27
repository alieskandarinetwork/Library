package model.dto.create;

public record CreatePersonDto(
        String name,
        String family,
        String nationalCode
) {
}

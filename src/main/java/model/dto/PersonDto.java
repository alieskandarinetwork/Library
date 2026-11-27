package model.dto;

import lombok.experimental.SuperBuilder;

public record PersonDto(
        Long id,
        String name,
        String family,
        String nationalCode
) {
}

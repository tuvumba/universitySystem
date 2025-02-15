package dev.tuvumba.theboringuniversity.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StudentDto {
    @Schema(example = "johndeer")
    private String username;
    @Schema(example = "John")
    private String name;
    @Schema(example = "Deer")
    private String surname;
}


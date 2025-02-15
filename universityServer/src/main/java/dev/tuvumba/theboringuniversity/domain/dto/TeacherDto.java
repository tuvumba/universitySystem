package dev.tuvumba.theboringuniversity.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TeacherDto {
    @Schema(example = "Gojo")
    private String name;
    @Schema(example = "Satoru")
    private String surname;
    @Schema(example = "jujitsukaisenismid")
    private String username;
    @Schema(example = "Ph.D Bc.")
    private String titles;
}


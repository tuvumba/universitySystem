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
public class SubjectDto {
    @Schema(example = "1")
    private Long id;
    @Schema(example = "Algorithms and Graphs 1")
    private String name;
    @Schema(example = "A subject for everyone to enjoy.")
    private String description;
}


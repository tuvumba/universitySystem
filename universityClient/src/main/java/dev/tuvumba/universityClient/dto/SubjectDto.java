package dev.tuvumba.universityClient.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SubjectDto {
    private Long id;
    private String name;
    private String description;
}

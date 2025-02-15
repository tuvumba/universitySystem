package dev.tuvumba.universityClient.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TeacherDto {
    private String name;
    private String surname;
    private String username;
    private String titles;
}


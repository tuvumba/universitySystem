package dev.tuvumba.universityClient.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class StudentDto {
    private String username;
    private String name;
    private String surname;
}

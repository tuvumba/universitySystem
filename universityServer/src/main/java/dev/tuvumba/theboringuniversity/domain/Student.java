package dev.tuvumba.theboringuniversity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "enrolledSubjects")
public class Student {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Id
    private String username;

    @Getter
    @ManyToMany(mappedBy = "students")
    @JsonBackReference
    private Set<Subject> enrolledSubjects = new HashSet<>();

    @PreRemove
    private void preRemove() {
        for(Subject subject : enrolledSubjects) {
            subject.removeStudent(this);
        }
    }
}


package dev.tuvumba.theboringuniversity.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "taughtSubjects")
public class Teacher {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Id
    private String username;
    private String titles;

    @Getter
    @ManyToMany(mappedBy = "teachers")
    @JsonBackReference
    private Set<Subject> taughtSubjects = new HashSet<>();

    @PreRemove
    private void preRemove() {
        for(Subject subject : taughtSubjects) {
            subject.removeTeacher(this);
        }
    }
}


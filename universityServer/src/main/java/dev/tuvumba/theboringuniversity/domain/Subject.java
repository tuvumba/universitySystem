package dev.tuvumba.theboringuniversity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @ManyToMany
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_username")
    )
    @JsonManagedReference
    private Set<Teacher> teachers = new HashSet<>();

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.getTaughtSubjects().add(this);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        teacher.getTaughtSubjects().remove(this);
    }

    @Getter
    @ManyToMany
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "student_username")
    )
    @JsonManagedReference
    private Set<Student> students = new HashSet<>();

    public void addStudent(Student student) {
        students.add(student);
        student.getEnrolledSubjects().add(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.getEnrolledSubjects().remove(this);
    }

    @PreRemove
    public void preRemove() {
        for(Student student : students) {
            student.getEnrolledSubjects().remove(this);
        }

        for(Teacher teacher : teachers) {
            teacher.getTaughtSubjects().remove(this);
        }
    }

}

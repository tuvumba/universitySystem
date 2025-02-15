package dev.tuvumba.theboringuniversity.repository;

import dev.tuvumba.theboringuniversity.domain.Student;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {
    List<Student> findByNameContainingIgnoreCase(String name, Limit limit);
    List<Student> findBySurnameContainingIgnoreCase(String surname, Limit limit);
}


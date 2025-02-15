package dev.tuvumba.theboringuniversity.repository;

import dev.tuvumba.theboringuniversity.domain.Teacher;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, String> {
    @Query("""
                SELECT t FROM Teacher t
                JOIN t.taughtSubjects s
                JOIN s.students st
                GROUP BY t
                ORDER BY COUNT(st) DESC
                LIMIT 1
            """)
    Teacher findTeacherWithMostStudents();

    List<Teacher> findByNameContainingIgnoreCase(String query, Limit limit);
    List<Teacher> findBySurnameContainingIgnoreCase(String query, Limit limit);
}


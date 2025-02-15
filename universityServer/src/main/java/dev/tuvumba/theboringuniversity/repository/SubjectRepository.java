package dev.tuvumba.theboringuniversity.repository;

import dev.tuvumba.theboringuniversity.domain.Subject;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    Optional<Subject> findByName(String name);

    List<Subject> findByNameContaining(String name, Limit limit);
}

